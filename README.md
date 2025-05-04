# Informationen zur Entwicklung von Bilanzius

### Protokoll
Das Protokoll zum Programmentwurf befindet sich in der README.pdf.

### Architketur

Main <-> CommandController <-> Command1 / Command2 / ... <-> Andere Services / ...

### Ordner

- Der "services"-Ordner ist für alle Features
- Dort gibt es den "commands"-Ordner um direkt den jeweiligen Befehl umzugehen
- Weiter Ordner in "services" können erstellt werden um dort andere Klassen zu speichern und zu nutzten

### Klassen

- Main-Klasse sollte nur in notwendigen Fällen geändert werden
- CommandController wird nur für die Befehle genutzt und sollte so wenig Logik wie möglich beinhalten
- Jeder Befehl bekommt eine Klasse, in der wird eine "Haupt-Methode" definiert, in der dann der Befehl verarbeitet wird und dort sollen dann andere Klassen und Methoden angesprochen werden

### Branches

- Jeder sollte auf seinem Branch bleiben
- Main-Branch muss immer funktionieren
- Auf Main-Branch wird nur gepusht, wenn ein Feature funktioniert und von den anderen geprüft wurde

