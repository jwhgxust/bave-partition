package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition

import java.lang.Math.pow

interface ICDroppingProbability {
    val baveLength: Double
    //========================================
    val averageDroppingProbability: Double
    val droppingUniformity: Double
    val minDroppingPosRatio: Double
    //========================================
    val minDroppingPos: Double
        get() = minDroppingPosRatio * baveLength
    val minDroppingProbability: Double
        get() = droppingUniformity * averageDroppingProbability
    val averageDroppingTimes: Double
        get() = averageDroppingProbability * baveLength
    val reelability: Double
        get() = 1.0 / (averageDroppingTimes + 1.0)

    //========================================
    fun integrate(startPos: Double, endPos: Double): Double

    //========================================
    val sum: Double
        get() = integrate(0.0, baveLength)
    val average: Double
        get() = sum / baveLength

    //========================================
    fun dropping(pos: Double): Double

    fun notDropping(pos: Double): Double = 1.0 - dropping(pos)
    //========================================
    fun nonBrokenSegmentProbability(startPos: Double, endPos: Double): Double {
        if (startPos < 0 || startPos > baveLength) {
            throw IllegalArgumentException("Expected the parameter {0 <= startPos <= $baveLength},but get {startPos = $startPos}.")
        }
        if (endPos < 0 || endPos > baveLength) {
            throw IllegalArgumentException("Expected the parameter {0 <= endPos <= $baveLength},but get {endPos = $endPos}.")
        }
        if (startPos > endPos) {
            throw IllegalArgumentException("Expected the parameter {startPos <= endPos},but get {startPos = $startPos,endPos = $endPos}.")
        }
        return Math.exp(-integrate(startPos, endPos))
    }
}

interface ICQuadraticDroppingProbability : ICDroppingProbability {
    val modelCoef: Double
        get() = (3.0 * averageDroppingProbability * (1.0 -
                droppingUniformity)) / (pow(baveLength,
                2.0) * (1.0 - 3.0 * minDroppingPosRatio +
                3.0 * pow(minDroppingPosRatio, 2.0)))
    //========================================
    val quadraticVertexA: Double
        get() = modelCoef
    val quadraticVertexB: Double
        get() = minDroppingPos
    val quadraticVertexC: Double
        get() = minDroppingProbability

    fun value(x: Double): Double = quadraticVertexA * pow(x - quadraticVertexB, 2.0) + quadraticVertexC

    fun integral(x: Double): Double {
        return quadraticVertexA * pow(quadraticVertexB, 2.0) * x +
                quadraticVertexC * x - quadraticVertexA * quadraticVertexB *
                pow(x, 2.0) + (quadraticVertexA * pow(x, 3.0)) / 3.0
    }

    fun integral(x0: Double, x1: Double) = integral(x1) - integral(x0)
    //========================================
    override fun integrate(startPos: Double, endPos: Double): Double {
        if (startPos < 0 || startPos > baveLength) {
            throw IllegalArgumentException("Expected the parameter {0 <= startPos <= $baveLength},but get {startPos = $startPos}.")
        }
        if (endPos < 0 || endPos > baveLength) {
            throw IllegalArgumentException("Expected the parameter {0 <= endPos <= $baveLength},but get {endPos = $endPos}.")
        }
        if (startPos > endPos) {
            throw IllegalArgumentException("Expected the parameter {startPos <= endPos},but get {startPos = $startPos,endPos = $endPos}.")
        }
        return integral(startPos, endPos)
    }

    override fun dropping(pos: Double): Double {
        if (pos < 0 || pos > baveLength) {
            throw IllegalArgumentException("Expected the parameter {0 <= pos <= $baveLength},but get {pos = $pos}.")
        }
        return value(pos)
    }

}

open class CQuadraticDroppingProbability(final override val baveLength: Double,
                                         final override val averageDroppingProbability: Double,
                                         final override val droppingUniformity: Double,
                                         final override val minDroppingPosRatio: Double) : ICQuadraticDroppingProbability {
    init {
        if ((!baveLength.isFinite()) || baveLength <= 0.0) {
            throw IllegalArgumentException("Expected the parameter {baveLength > 0.0} and is finite,but get {baveLength = $baveLength}.")
        }
        if ((!averageDroppingProbability.isFinite()) || averageDroppingProbability <= 0.0) {
            throw IllegalArgumentException("Expected the parameter {averageDroppingProbability > 0.0} and is finite,but get {averageDroppingProbability = $averageDroppingProbability}.")
        }
        if ((!droppingUniformity.isFinite()) || droppingUniformity < 0.0 || droppingUniformity > 1.0) {
            throw IllegalArgumentException("Expected the parameter {0.0 <= droppingUniformity <= 1.0} and is finite,but get {droppingUniformity = $droppingUniformity}.")
        }
        if ((!minDroppingPosRatio.isFinite()) || minDroppingPosRatio < 0.0 || minDroppingPosRatio > 1.0) {
            throw IllegalArgumentException("Expected the parameter {0.0 <= minDroppingPosRatio <= 1.0} and is finite,but get {minDroppingPosRatio = $minDroppingPosRatio}.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CQuadraticDroppingProbability) return false
        if (baveLength != other.baveLength) return false
        if (averageDroppingProbability != other.averageDroppingProbability) return false
        if (droppingUniformity != other.droppingUniformity) return false
        if (minDroppingPosRatio != other.minDroppingPosRatio) return false
        return true
    }

    override fun hashCode(): Int {
        var result = baveLength.hashCode()
        result = 31 * result + averageDroppingProbability.hashCode()
        result = 31 * result + droppingUniformity.hashCode()
        result = 31 * result + minDroppingPosRatio.hashCode()
        return result
    }

    override fun toString(): String {
        return "CQuadraticDroppingProbability(L=$baveLength, beta=$averageDroppingProbability, C=$droppingUniformity, rho=$minDroppingPosRatio)"
    }
}