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
): BaseViewModel(), LifecycleObserver{

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> = _friends

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume(){
        viewModelScope.launch(dispatchersIO) { getFriends() }
    }

    private suspend fun getFriends() {
        if (sessionManager.isNetworkAvailable()){
            val friendsList = repository.getFriends()
            if (friendsList != null ) { _friends.postValue(friendsList!!) }
            else { postMessage(R.string.cant_get_friends) } //todo jak dodaje nowego znajomego to ta wiadomosc za szybko sie pojawia i pokrywa sie z ta ze znajomy dodany
        } else { postMessage(R.string.connection_problem) }
    }

    suspend fun logout() {
        withContext(dispatchersIO){ repository.logout() }
    }

    suspend fun addFriendByEmail(email: String){
        withContext(dispatchersIO){
            if (sessionManager.isNetworkAvailable()) {
                repository.addFriendByEmail(email){ message ->
                    viewModelScope.launch(dispatchersIO){
                        postMessage(message)
                        getFriends() //todo pozniej po zamianie, dawac to wywolanie jak jest sukces, bo teraz jest wywolywana nawet jak failure
                    }
                }
            }
            else { postMessage(R.string.connection_problem) }
        }
    }

}