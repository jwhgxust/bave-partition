package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition.stepwise

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition.CPartitionPattern
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition.CQuadraticDroppingProbability
import cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition.ICQuadraticPartitioner
import org.hipparchus.analysis.solvers.UnivariateSolverUtils

class CStepwisePartitioner(
        baveLength: Double,
        averageDroppingProbability: Double,
        droppingUniformity: Double,
        minDroppingPosRatio: Double,
        override val randomDataGenerator: RandomDataGenerator = RandomDataGenerator()
) : CQuadraticDroppingProbability(baveLength, averageDroppingProbability, droppingUniformity, minDroppingPosRatio),
        ICQuadraticPartitioner {

    fun nextPosDroppingProbability(startPos: Double, nextPos: Double): Double {
        return dropping(nextPos) * nonBrokenSegmentProbability(startPos, nextPos)
    }

    fun nextDroppingPos(startPos: Double): Double {
        val rand = randomDataGenerator.nextDouble()
        val notProbability = nonBrokenSegmentProbability(startPos, baveLength)
        if (rand <= notProbability) {
            return baveLength
        } else {
            val nextPosProbability: (Double) -> Double = fun(nextPos: Double): Double {
                return (1.0 - nonBrokenSegmentProbability(startPos, nextPos)) - (rand - notProbability)
            }
            return UnivariateSolverUtils.solve(nextPosProbability, startPos, baveLength, 1e-15)
        }
    }

    override fun partition(): CPartitionPattern {
        val result = mutableListOf<Double>()
        var startPos = 0.0
        result.add(startPos)
        while (true) {
            val nextPos = nextDroppingPos(startPos)
            result.add(nextPos)
            if (nextPos == baveLength) {
                break
            } else {
                startPos = nextPos
            }
        }
        return CPartitionPattern(baveLength, result)
    }
}

fun main() {
    val partitioner = CStepwisePartitioner(
            1000.0,
            (1.0 / 0.8 - 1.0) / 1000.0,
            0.5, 0.3
    )
    val times = 1000000
    for (i in 1..times) {
        val pattern = partitioner.partition()
        System.out.println("droppingPoses = ${pattern.droppingPoses}, nonBrokenLengths = ${pattern.nonBrokenLengths}.")
    }
}