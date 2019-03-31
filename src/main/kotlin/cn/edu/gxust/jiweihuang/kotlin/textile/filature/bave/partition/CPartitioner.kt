package cn.edu.gxust.jiweihuang.kotlin.textile.filature.bave.partition

import cn.edu.gxust.jiweihuang.kotlin.math.stat.random.RandomDataGenerator

interface ICPartitioner:ICDroppingProbability{
    val randomDataGenerator: RandomDataGenerator
    fun partition(): ICPartitionPattern
}

interface ICQuadraticPartitioner:ICPartitioner,ICQuadraticDroppingProbability
