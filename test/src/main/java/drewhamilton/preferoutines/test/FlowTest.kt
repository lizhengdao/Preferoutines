package drewhamilton.preferoutines.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.newSingleThreadContext
import org.junit.After
import org.junit.Before
import kotlin.coroutines.CoroutineContext

abstract class FlowTest {

    private lateinit var testContext: CoroutineContext
    private lateinit var testScope: CoroutineScope

    private val testCollectors: MutableCollection<TestCollector<*>> = mutableSetOf()

    @OptIn(ObsoleteCoroutinesApi::class)
    @Before
    fun setUpTestScope() {
        testContext = newSingleThreadContext("Test context")
        testScope = CoroutineScope(testContext)
    }

    @After
    fun cancelAllJobs() {
        for (testCollector in testCollectors) {
            testCollector.deferred.cancel()
        }
        testContext.cancelChildren()
        testContext.cancel()
    }

    protected fun <T> Flow<T>.test(): TestCollector<T> {
        val testCollector = TestCollector<T>()
        testCollector.deferred = testScope.async(testContext) {
            collect { value ->
                testCollector.values.add(value)
            }
        }
        testCollectors.add(testCollector)
        return testCollector
    }

}
