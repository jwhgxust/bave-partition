package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator

interface ICPartitionSystem {
    val randomDataGenerator: RandomDataGenerator
}

interface ICQuadraticPartitionSystem : ICPartitionSystem {
    fun createQuadraticPartitioner(
            baveLength: Double,
            averageDroppingProbability: Double,
            droppingUniformity: Double,
            minDroppingPosRatio: Double): ICQuadraticPartitioner

    fun startSim(iterations: Long):CPartitionRecorder<ICPartitionPattern>

}