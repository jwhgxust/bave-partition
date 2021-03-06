package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition
/**
 * The interface [ICPartitionPattern] is used for recording
 * the dropping position on bave that be executed by partitioner.
 *
 * Create date:2019-02-21.
 *
 * Last revision date:2019-02-21.
 *
 *
 * @author Jiwei Huang
 * @since 1.0.0
 */
interface ICPartitionPattern {
    /**
     * The length of bave.
     */
    val baveLength: Double

    /**
     * The dropping position on bave.
     *
     * It is required that elements is in ascending order.
     *
     * It has to include the head ([0]) and tail ([baveLength]) position of bave.
     */
    val droppingPoses: List<Double>

    /**
     * filter out the head ([0]) and tail ([baveLength]) position of bave from
     * [droppingPoses].
     */
    val midwayDroppingPoses: List<Double>
        get() = droppingPoses.filter { it != 0.0 && it != baveLength }

    /**
     * The list of non-broken filament length.
     */
    val nonBrokenLengths: List<Double>
        get() {
            val makeList = mutableListOf<Double>()
            for (i in 1 until droppingPoses.size) {
                if (droppingPoses[i] <= droppingPoses[i - 1]) {
                    throw RuntimeException("Expected the dropping pos is order increasing sequence,but get {[i=$i] = ${droppingPoses[i]} and [i-1=${i - 1}] = ${droppingPoses[i - 1]}}.")
                }
                makeList.add(droppingPoses[i] - droppingPoses[i - 1])
            }
            return makeList
        }

    /**
     * The number of non-broken filament segment.
     */
    val nonBrokenNum: Int
        get() = nonBrokenLengths.size
    /**
     * The dropping times.
     */
    val droppingTimes: Int
        get() {
            if (droppingPoses.size < 2) {
                throw RuntimeException("Expected {droppingPoses.size >= 2},but get {droppingPoses.size =${droppingPoses.size}}.")
            }
            return droppingPoses.size - 2
        }
}

open class CPartitionPattern(
        final override val baveLength: Double,
        final override val droppingPoses: List<Double>
) : ICPartitionPattern {
    init {
        if (!baveLength.isFinite() || baveLength <= 0) {
            throw IllegalArgumentException("Expected the parameter {baveLength > 0.0} and finite,but get {baveLength = $baveLength}.")
        }
        if (droppingPoses.size < 2) {
            throw IllegalArgumentException("Expected {droppingPoses.size >= 2},but get {droppingPoses.size =${droppingPoses.size}}.")
        }
        for (i in 1 until droppingPoses.size) {
            if (droppingPoses[i] <= droppingPoses[i - 1]) {
                throw RuntimeException("Expected the dropping pos is order increasing sequence,but get {[i=$i] = ${droppingPoses[i]} and [i-1=${i - 1}] = ${droppingPoses[i - 1]}}.")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CPartitionPattern) return false
        if (baveLength != other.baveLength) return false
        if (droppingPoses != other.droppingPoses) return false
        return true
    }

    override fun hashCode(): Int {
        var result = baveLength.hashCode()
        result = 31 * result + droppingPoses.hashCode()
        return result
    }

    override fun toString(): String {
        return "CPartitionPattern(L=$baveLength, droppingPoses=$droppingPoses)"
    }

}