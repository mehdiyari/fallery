package ir.mehdiyari.fallery.buckets.ui.bucketContent

import ir.mehdiyari.fallery.models.BucketType
import ir.mehdiyari.fallery.models.Media
import ir.mehdiyari.fallery.repo.AbstractBucketContentProvider
import ir.mehdiyari.fallery.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class BucketContentViewModel constructor(
    private val abstractBucketContentProvider: AbstractBucketContentProvider,
    private val bucketType: BucketType
) : BaseViewModel() {

    @ExperimentalCoroutinesApi
    private val medias = MutableStateFlow<List<Media>>(listOf())

    @ExperimentalCoroutinesApi
    val mediaList: StateFlow<List<Media>> = medias

    @ExperimentalCoroutinesApi
    fun getMedias(bucketId: Long) {
        if (mediaList.value.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            abstractBucketContentProvider.getMediasOfBucket(bucketId, bucketType)
                .collect {
                    medias.value = mediaList.value.toMutableList().apply { addAll(it) }.toList()
                }
        }
    }
}