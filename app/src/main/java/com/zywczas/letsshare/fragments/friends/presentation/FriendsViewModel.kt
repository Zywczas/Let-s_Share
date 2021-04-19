package com.zywczas.letsshare.fragments.friends.presentation

import androidx.lifecycle.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.activitymain.presentation.BaseViewModel
import com.zywczas.letsshare.di.modules.DispatchersModule.DispatchersIO
import com.zywczas.letsshare.fragments.friends.domain.FriendsRepository
import com.zywczas.letsshare.model.Friend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher,
    private val sessionManager: SessionManager,
    private val repository: FriendsRepository
): BaseViewModel(){

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends

    suspend fun getFriends() {
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()){
                showProgressBar(true)
                repository.getFriends()?.let {
                    _friends.postValue(it)
                    repository.saveFriendsLocally(it)
                } ?: kotlin.run { postMessage(R.string.cant_get_friends) }
                showProgressBar(false)
            } else { postMessage(R.string.connection_problem) }
        }
    }

    suspend fun logout() {
        withContext(dispatchersIO){ repository.logout() }
    }

    suspend fun addFriend(email: String){
        withContext(dispatchersIO) {
            showProgressBar(true)
            when {
                sessionManager.isNetworkAvailable().not() -> postMessage(R.string.connection_problem)
                repository.userEmail() == email -> postMessage(R.string.its_your_email)
                isFriendAlreadyAdded(email) -> postMessage(R.string.you_have_this_friend)
                else -> repository.addFriend(email)?.let { error -> postMessage(error) }
                    ?: getFriends() //todo jak bede nasluchiwac bazy to to bede mogl usunac
            }
            showProgressBar(false)
        }
    }

    private fun isFriendAlreadyAdded(email: String): Boolean =
        friends.value?.any { it.email == email } == true

}