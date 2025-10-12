db = db.getSiblingDB('notesdb');

db.notes.drop();

db.notes.insertMany([
    { patId: 1, patient: "TestNone", note: "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé", date: new Date("2025-10-01T10:00:00Z") },
    { patId: 2, patient: "TestBorderline", note: "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement", date: new Date("2025-10-02T11:00:00Z") },
    { patId: 2, patient: "TestBorderline", note: "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale", date: new Date("2025-10-03T12:00:00Z") },
    { patId: 3, patient: "TestInDanger", note: "Le patient déclare qu'il fume depuis peu", date: new Date("2025-10-04T09:00:00Z") },
    { patId: 3, patient: "TestInDanger", note: "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé", date: new Date("2025-10-05T14:30:00Z") },
    { patId: 4, patient: "TestEarlyOnset", note: "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments", date: new Date("2025-10-06T08:15:00Z") },
    { patId: 4, patient: "TestEarlyOnset", note: "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps", date: new Date("2025-10-07T10:45:00Z") },
    { patId: 4, patient: "TestEarlyOnset", note: "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé", date: new Date("2025-10-08T13:00:00Z") },
    { patId: 4, patient: "TestEarlyOnset", note: "Taille, Poids, Cholestérol, Vertige et Réaction", date: new Date("2025-10-09T09:30:00Z") }
]);