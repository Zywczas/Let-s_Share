package com.zywczas.letsshare.utils

//todo zamienic shared prefs n Jetpack DataStore
//todo dac tak: app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
//todo nie pojawia sie info o weryfikacji maila, trzeba dac alert dialog i to info
//todo dac wylogowywanie na guziku
//todo dac wylogowywanie zaraz po zalogowaniu, zeby potwierdzic maila najpierw
//todo dac potwierdzenie maila
//todo dodac fejsa
//            val token = AccessToken.getCurrentAccessToken()
//            isLoggedIn = token != null && token.isExpired.not()
//todo dodac progress bar do wszystkich ladowan
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
//todo dodac nasluchiwanie do grup, czy jakas sie nie pokazala w ktorej jestesmy
//todo poustawiac tytuly dla wszystkich toolbar i usunac wszystkie ich label z nav graf
//todo dac pozniej groups fragment jako pierwszy, przed friends
//todo przy klikaniu na nav bottom bar dac czyszczenie stacka, spradzic kilkajac z group details do friends np.
//todo dac nienulowalne array dla usera
//todo to dodawanie znajomych i grup dac w alertdialogach
//todo dodac ograniczenia ilosci znakow w edit tekstach dla hasel, imion, nazw, itd...
//todo sprawdzic czy jak dam crashlytics to moge usunac wszystkie logi z programu
//todo czyscic backstack przy klikaniu w bottom nav bar bo jak sie wciska wstecz to cofa do fragmentu ale nie odswieza kliknietego nav bar
//todo dodac swipe to refresh w groups fragment
//todo dodac sprawdzenie czy miesiac nie jest juz stary, i wtedy zablokowac operacje dopoki nie zostanie rozliczony
//todo dodac crashlitycs do wszystkich catch w repo
//todo jak group details bedzie za wolno sie ladowac to doac to przed ustawianiem guzikow
//todo jak nie bedzie neta to dac info w footerze wszystkich recycler view
//todo przy pobierniu friends najpierw pobierac z bazy danych, a pozniej w miedzy czasie aktualizowac z neta
//todo poprawic wyglad listy Firends - ukryc maila, moze pokazac po dluzszym nacisnieciu na niego :)
//todo nasluchiwac nowuch uzytkownikoww grupie
//todo nasluchiwac nowych wydatkow w grupie


private fun test(){
//    firestore.runTransaction {  } //todo dobra funkcja do updatowania, jak jakis dokument w miedzy czasie sie zmieni to transakcja leci od nowa
//    FieldValue.increment(23.0) //todo to jest dobbre to zwiekszania wartosci bez koniecznosci pobierania wartosci przed updatem
//    FieldValue.arrayUnion(...)
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