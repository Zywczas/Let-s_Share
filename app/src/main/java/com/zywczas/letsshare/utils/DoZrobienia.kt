package com.zywczas.letsshare.utils

//todo dac tak: app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
//todo nie pojawia sie info o weryfikacji maila, trzeba dac alert dialog i to info
//todo dac wylogowywanie na guziku
//todo dac wylogowywanie zaraz po zalogowaniu, zeby potwierdzic maila najpierw
//todo dac potwierdzenie maila
//todo dodac fejsa
//            val token = AccessToken.getCurrentAccessToken()
//            isLoggedIn = token != null && token.isExpired.not()
//todo pousuwac logi
//todo sprawdzic czy za pomoca fejsbuka mozna sie zalogowac jak nie ma sie polaczenia internetowego
//todo dodac sprawdzenie czy jest google play i czy jest zalogowany
//todo dodac animacje do przejsc miedzy fragmentami
//todo dodac progress bar przy rejestracji i logowaniu
//todo dodac podwojne wpisywanie hasla i weryfikacje czy sa takie same
//todo dodac podwojne wpisywanie emaila i weryfikacje czy sa takie same
//todo przy rejestracji dodac akcje na guziku niebieski z klawiatury
//todo w shared prefs albo roomie powinno byc zapisywanie czy dany uzytkownik jest zalogowany, bo jak nie ma internetu to tez powinien byc w stanie odczytac liste i dodawac koszty, a wtedy jak
//juz bedzie internet to odwiezy dane
//todo poustawiac label dla wszystkich destination
//todo potworzyc rules i indexy - jak opisane w zeszycie
//todo dodac sprawdzenie czy ktos nie dodaje znajomego podajac swoj email przez przypadek
//todo dodac sprawdzenie czy ktos nie dodaje znajomego, ktory juz jest jego znajomym
//todo sprawdzic czy znajomi sa posortowani wg daty
//todo dac logowanie z google
//todo dac zapisywanie listy zakupow lokalnie i ladowanie w tle z neta, jesli poloaczony, uzyc firebase offline data
//todo dodac pozniej potwierdzenia czy ktos chce dolaczyc do grupy
//todo czesto dobre jest nasluchiwanie danych z bazy, jak np sprawdzamy sume czegos, bo w miedzy czasie np. dane moga sie zmienic
//todo przy dodawaniu znajomego dawac potwierdzenie ze chce sie byc znajomym, i wtedy dopiero moze pokazac obu, nie wiem czy to dobrze ze mowi jak kogos nie ma
//todo jak sie doda znajomego to znajomy tez od razu powinien nas widziec i miec w nas w swoich znajomych
//todo dac side nav rail dla tabletow
//todo sprawdzi czy jak bottom nav bar jest klikany na tym samym guziki to czy fragment sie od nowa nie laduje czasem
//todo nie ma ladnego przejscia miedzy fragmentami jak klikam na nav bar, dac przejscie akcjami

private fun test(){
//    firestore.runTransaction {  } //todo dobra funkcja do updatowania, jak jakis dokument w miedzy czasie sie zmieni to transakcja leci od nowa
//    FieldValue.increment(23.0) //todo to jest dobbre to zwiekszania wartosci bez koniecznosci pobierania wartosci przed updatem
}
//przyklad transakcji
//val sfDocRef = db.collection("cities").document("SF")
//
//db.runTransaction { transaction ->
//    val snapshot = transaction.get(sfDocRef)
//
//    // Note: this could be done without a transaction
//    //       by updating the population using FieldValue.increment()
//    val newPopulation = snapshot.getDouble("population")!! + 1
//    transaction.update(sfDocRef, "population", newPopulation)
//
//    // Success
//    null
//}.addOnSuccessListener { Log.d(TAG, "Transaction success!") }
//.addOnFailureListener { e -> Log.w(TAG, "Transaction failure.", e) }

//todo batche sa do update i tworzenia kilku dokumentow na raz
//val nycRef = db.collection("cities").document("NYC")
//val sfRef = db.collection("cities").document("SF")
//val laRef = db.collection("cities").document("LA")
//
//// Get a new write batch and commit all write operations
//db.runBatch { batch ->
//    // Set the value of 'NYC'
//    batch.set(nycRef, City())
//
//    // Update the population of 'SF'
//    batch.update(sfRef, "population", 1000000L)
//
//    // Delete the city 'LA'
//    batch.delete(laRef)
//}.addOnCompleteListener {
//    // ...
//}