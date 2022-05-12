package ru.maxmv.whack_a_mole.presentation.game

import android.os.CountDownTimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _isGameOverLiveData = MutableLiveData<Boolean>()
    val isGameOverLiveData: LiveData<Boolean> = _isGameOverLiveData

    private val _timeLiveData = MutableLiveData<Long>()
    val timeLiveData: LiveData<Long> = _timeLiveData

    fun countTime() {
        object : CountDownTimer(30000, 1) {

            override fun onTick(millisUntilFinished: Long) {
                _timeLiveData.postValue(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                _isGameOverLiveData.postValue(true)
            }
        }.start()
    }
}