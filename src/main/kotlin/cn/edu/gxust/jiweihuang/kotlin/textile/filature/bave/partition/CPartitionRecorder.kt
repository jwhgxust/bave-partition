package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition

import cn.edu.gxust.jiweihuang.kotlin.math.stat.descriptive.Frequency
import java.util.*

class CPartitionRecorder<V: ICPartitionPattern> {
    private val partitionPatterns: NavigableMap<String, V> = TreeMap<String, V>()

    constructor()
    constructor(partitionPatterns: Map<String, V>) {
        this.partitionPatterns.putAll(partitionPatterns)
    }

    fun addPartition(key: String, partitionPatterns: V) {
        this.partitionPatterns[key] = partitionPatterns
    }

    fun clear() {
        partitionPatterns.clear()
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder("DPartitionRecorder:\n")
        for (key in partitionPatterns.keys) {
            stringBuilder.append("$key=")
            stringBuilder.append(partitionPatterns[key])
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    fun droppingPosesFrequency(): Frequency<Double> {
        val frequency: Frequency<Double> = Frequency()
        for (pp in partitionPatterns.values) {
            frequency.add(pp.droppingPoses)
        }
        return frequency
    }

    fun midwayDroppingPosesFrequency(): Frequency<Double> {
        val frequency: Frequency<Double> = Frequency()
        for (pp in partitionPatterns.values) {
            frequency.add(pp.midwayDroppingPoses)
        }
        return frequency
    }

    fun baveLengthFrequency(): Frequency<Double> {
        val frequency: Frequency<Double> = Frequency()
        for (pp in partitionPatterns.values) {
            frequency.addValue(pp.baveLength)
        }
        return frequency
    }

    fun nonBrokenLengthFrequency(): Frequency<Double> {
        val frequency: Frequency<Double> = Frequency()
        for (pp in partitionPatterns.values) {
            frequency.add(pp.nonBrokenLengths)
        }
        return frequency
    }

    fun droppingTimesFrequency(): Frequency<Int> {
        val frequency: Frequency<Int> = Frequency()
        for (pp in partitionPatterns.values) {
            frequency.addValue(pp.droppingTimes)
        }
        return frequency
    }
}