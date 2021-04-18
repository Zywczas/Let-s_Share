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
                repository.getFriends()?.let { _friends.postValue(it) }
                    ?: kotlin.run { postMessage(R.string.cant_get_friends) }
                showProgressBar(false)
            } else { postMessage(R.string.connection_problem) }
        }
    }

    suspend fun logout() {
        withContext(dispatchersIO){ repository.logout() }
    }

    suspend fun addFriend(email: String){
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()) {
                repository.addFriendByEmail(email){ message ->
                    viewModelScope.launch(dispatchersIO){
                        postMessage(message)
                        getFriends() //todo pozniej po zamianie, dawac to wywolanie jak jest sukces, bo teraz jest wywolywana nawet jak failure, ale jak bedzie nasluchiwanie bazy to tego nie trzeba dawac
                    }
                }
            }
            else { postMessage(R.string.connection_problem) }
        }
    }

}