package com.zywczas.letsshare.utils

//todo poprawic kolory w dark mode
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
//todo sprawdzic czy live data puszcza value nawet jak nie ma observers
//todo dac pozniej cos takiego jak groupRef() = firestore.coll.doc
//todo zamienic pozniej double na BigDecimal wszedzie gdzie kwoty i udzialy
//todo launchWhenResumed dac tylko do klikania guzikow, a na start normalne launch
//todo przejrzec wszystkie klasy i pousuwac komentarze
//todo w gruopdetails dac podluzny progres bar zeby kolko sie ciagle tam nie krecilo
//todo zamiast dawac date w Polsce to trzeba dac date default zeby dla kazdego kraju byla odpowiednia
//todo jak sie dodaje znajomego to w jego grupie friends tez powinien pokazywac sie nowy znajomy
//todo jak sie usuwa znajomego to usunac go tez z rooma, albo dac czyszczenie bazy przed kazdym updatem
//todo dodac guziki anuluj do dialogow
//todo poustawiac motywy dla wszystkich guzikow, paskow i innych
//todo dac kto sie ostatnio logowal, zeby maila samo wpisywalo
//todo dac input edit texty z ramka, tak jak w OTS w R.layout.item_type_key_value
//todo zmienic kolor paska dolnego, bo na moim telefonie jest bialy i sie wyroznia od zielonego
//todo zmienic ikonke apki an tak bez cienia i zobaczyc czy bedzie wyrazniejsza
//todo sortowanie w firestore po stringach jest bez sensu, sprawdzic czy nie ma gdzies sortowania i nie przeniesc na apke
//todo sprawdzic to glupie przeskakiwanie ekranu jak sie wchodzi w jakas grupe, moze zmienic sposob w jakis toolbar jest podawany, bo nie jest to oryginalne roziazanie

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