db = db.getSiblingDB('notesdb');

db.notes.drop();

db.notes.insertMany([
    { patId: 1, patient: "TestNone", note: "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé", date: "2025-10-01" },
    { patId: 2, patient: "TestBorderline", note: "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement", date: "2025-10-02" },
    { patId: 2, patient: "TestBorderline", note: "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale", date: "2025-10-03" },
    { patId: 3, patient: "TestInDanger", note: "Le patient déclare qu'il fume depuis peu", date: "2025-10-04" },
    { patId: 3, patient: "TestInDanger", note: "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé", date: "2025-10-05" },
    { patId: 4, patient: "TestEarlyOnset", note: "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments", date: "2025-10-06" },
    { patId: 4, patient: "TestEarlyOnset", note: "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps", date: "2025-10-07" },
    { patId: 4, patient: "TestEarlyOnset", note: "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé", date: "2025-10-08" },
    { patId: 4, patient: "TestEarlyOnset", note: "Taille, Poids, Cholestérol, Vertige et Réaction", date: "2025-10-09" }
]);