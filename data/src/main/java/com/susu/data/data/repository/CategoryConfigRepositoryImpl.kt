package com.susu.data.data.repository

import com.susu.core.android.Dispatcher
import com.susu.core.android.SusuDispatchers
import com.susu.core.model.Category
import com.susu.data.local.dao.CategoryConfigDao
import com.susu.data.remote.api.CategoryService
import com.susu.data.remote.model.response.toModel
import com.susu.domain.repository.CategoryConfigRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryConfigRepositoryImpl @Inject constructor(
    private val dao: CategoryConfigDao,
    private val api: CategoryService,
    @Dispatcher(SusuDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CategoryConfigRepository {

    private var cache: List<Category>? = null
    override suspend fun getCategoryConfig(): List<Category> = withContext(ioDispatcher) {
        // TODO: category config 캐싱 로직 삭제, 메모리 캐싱만 - 추후 room db 마이그레이션?
        return@withContext if (cache != null) {
            cache!!
        } else {
            val categories = api.getCategoryConfig().getOrThrow().map { it.toModel() }.sortedBy { it.seq }
            cache = categories
            categories
        }
    }
}
