@file:Suppress("UNCHECKED_CAST")

package kpk.dev.sportsbook.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * A helper function to create ViewModels with Dagger specifically in a composable context.
 * It allows you to provide a custom instance creator for the ViewModel, enabling
 * dependency injection with Dagger. The function is inlined so that the compiler can replace any calls at the call site.
 * Should provide some optimization benefits as we are accepting a function/lambda as a parameter.
 * the type T is reified, allowing access to the type at runtime, as we obviously need it get the actual class
 * The lambda is crossinlined to prevent any non-local returns, meaning it can only return from itself
 *
 * Usage:
 * ```
 * val myViewModel: MyViewModel = daggerViewModel {
 *     myViewModelFactory.create()
 * }
 * ```
 *
 * @param key An optional key to identify the ViewModel.
 * @param viewmodelInstanceCreator A lambda that returns an instance of the ViewModel.
 * @return An instance of the requested ViewModel.
 */
@Composable
inline fun <reified T : ViewModel> daggerViewModel(key: String? = null,
                                                   crossinline viewmodelInstanceCreator: () -> T): T  =
    viewModel(
        modelClass = T::class.java,
        key = key,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewmodelInstanceCreator() as T
            }
        }
    )