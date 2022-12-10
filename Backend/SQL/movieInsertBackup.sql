use flickpick;

-- INSERT INTO movies VALUES (id, title, minutes, year, rating, desc, pic);
-- INSERT INTO genres VALUES (id, genre1, genre2, genre3);
-- INSERT INTO links VALUES (id, netflix, hulu, hbo, disney, amazon);

-- INSERT INTO movies VALUES (, , , , , , );
-- INSERT INTO genres VALUES ( , , , );
-- INSERT INTO links VALUES ( , , , , , );

-- TOP 10 ACTION

INSERT INTO movies VALUES (1, 'Die Hard', 132, 1988, 0, 'An NYPD officer tries to save his wife and several others taken hostage by German terrorists during a Christmas party at the Nakatomi Plaza in Los Angeles.', '/Images/Thumbnails/dieHard.jpg');
INSERT INTO genres VALUES (1, 'action', 'thriller', '');
INSERT INTO links VALUES (1, '', 'https://www.hulu.com/movie/die-hard-efc8250c-aeed-4ab7-a492-07064c826bc1?entity_id=efc8250c-aeed-4ab7-a492-07064c826bc1', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.b0a9f6fa-a155-4fcf-3779-44b47105d67c?autoplay=0&ref_=atv_cf_strg_wb');


INSERT INTO movies VALUES (2,'Indiana Jones and the Raiders of the Lost Ark
' ,115 ,1981 ,0 ,'Archaeology professor Indiana Jones ventures to seize a biblical artefact known as the Ark of the Covenant. While doing so, he puts up a fight against Renee and a troop of Nazis.' , '/Images/Thumbnails/indianaJonesRLA.jpg');
INSERT INTO genres VALUES (2, 'action', 'adventure', '');
INSERT INTO links VALUES (2, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.fea9f760-ca31-43ec-e51b-75ad7db01298?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (3, 'Indiana Jones and the Temple of Doom', 158, 1984, 0, 'A skirmish in Shanghai puts archaeologist Indiana Jones, his partner Short Round and singer Willie Scott crossing paths with an Indian village desperate to reclaim a rock stolen by a secret cult beneath the catacombs of an ancient palace.
', '/Images/Thumbnails/indianaJonesTOD.jpg');
INSERT INTO genres VALUES (3, 'action', 'adventure', '');
INSERT INTO links VALUES (3, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.5aa9f6ff-5807-0059-2061-fc8f65041566?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (4, 'Indiana Jones and the Last Crusade', 127, 1989, 0, 'In 1938, after his father Professor Henry Jones, Sr. goes missing while pursuing the Holy Grail, Professor Henry \'Indiana\' Jones, Jr. finds himself up against Adolf Hitler\'s Nazis again to stop them from obtaining its powers.', '/Images/Thumbnails/indianaJonesLC.jpg');
INSERT INTO genres VALUES (4, 'action', 'adventure', '');
INSERT INTO links VALUES (4, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.f2a9f7b7-8f96-80d2-0724-5bbf73a63976?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (5, 'Terminator 2: Judgment Day', 137, 1991, 0, 'A cyborg, identical to the one who failed to kill Sarah Connor, must now protect her ten-year-old son John from a more advanced and powerful cyborg.','/Images/Thumbnails/terminator2.jpg' );
INSERT INTO genres VALUES (5, 'action', 'sci-fi', '');
INSERT INTO links VALUES (5, '', '', 'https://play.hbomax.com/feature/urn:hbo:feature:GYkHOsA0Pn5_CwwEAAAAV?source=googleHBOMAX&action=play', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.0cb28a4b-0a5a-8d11-0744-c06c0be3c057?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (6, 'Predator', 107, 1987, 0, 'A team of commandos on a mission in a Central American jungle find themselves hunted by an extraterrestrial warrior.', '/Images/Thumbnails/predator.jpg');
INSERT INTO genres VALUES (6, 'action', 'adventure', 'horror');
INSERT INTO links VALUES (6, '', 'https://www.hulu.com/watch/784d3d44-0cf3-429c-a372-bbf8ecbedba7', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.1ca9f785-c1b0-9710-a037-fc5f3893d65b?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (7, 'The Road Warrior', 96, 1981, 0, 'In the post-apocalyptic Australian wasteland, a cynical drifter agrees to help a small, gasoline-rich community escape a horde of bandits', '/Images/Thumbnails/theRoadWarrior.jpg');
INSERT INTO genres VALUES (7, 'action', 'adventure', 'sci-fi');
INSERT INTO links VALUES (7, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.88a9f770-f011-788a-38eb-7c5c139c00d0?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (8, 'RoboCop', 102, 1987, 0, 'In a dystopic and crime-ridden Detroit, a terminally wounded cop returns to the force as a powerful cyborg haunted by submerged memories.', '/Images/Thumbnails/roboCop.jpg');
INSERT INTO genres VALUES (8, 'action', 'adventure', 'sci-fi');
INSERT INTO links VALUES (8, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.1ca9f7bb-5db9-291a-3a59-3b3dd3daff52?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (9, 'Aliens', 102, 1987, 0, 'Fifty-seven years after surviving an apocalyptic attack aboard her space vessel by merciless space creatures, Officer Ripley awakens from hyper-sleep and tries to warn anyone who will listen about the predators.', '/Images/Thumbnails/aliens.jpg');
INSERT INTO genres VALUES (9, 'action', 'adventure', 'sci-fi');
INSERT INTO links VALUES (9, '', 'https://www.hulu.com/watch/b2ae8903-e3c5-4ae1-a9f7-885c04985f96', '', '' , 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.c0a9f7a6-4596-0e51-25e5-a2f5d5d370c8?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (10, 'Point Break', 122, 1991, 0, 'An F.B.I. Agent goes undercover to catch a gang of surfers who may be bank robbers.', '/Images/Thumbnails/pointBreak.jpg');
INSERT INTO genres VALUES (10, 'action', 'crime', 'thriller');
INSERT INTO links VALUES (10, 'https://www.netflix.com/watch/60020602?source=35', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.3ca9f736-6a1b-0368-0eb6-95077f51b5c3?autoplay=0&ref_=atv_cf_strg_wb');

-- TOP 10 Horror

INSERT INTO movies VALUES (11, 'Alien', 117, 1979, 0, 'The crew of a commercial spacecraft encounter a deadly lifeform after investigating an unknown transmission', '/Images/Thumbnails/alien.jpg');
INSERT INTO genres VALUES (11, 'horror', 'sci-fi', '');
INSERT INTO links VALUES (11, '', 'https://www.hulu.com/movie/alien-27389b6b-bf27-45a6-afdf-cef0fe723cff?entity_id=27389b6b-bf27-45a6-afdf-cef0fe723cff', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.6aa9f7a3-011e-77e1-5089-bd36198cd713?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (12, 'The Shining', 146, 1980, 0, 'A family heads to an isolated hotel for the winter where a sinister presence influences the father into violence, while his psychic son sees horrific forebodings from both past and future.', '/Images/Thumbnails/theShining.jpg');
INSERT INTO genres VALUES (12, 'drama', 'horror', '');
INSERT INTO links VALUES (12, '', '', 'https://play.hbomax.com/feature/urn:hbo:feature:GXmZ1WQgtCSLCHAEAABkh?source=googleHBOMAX&action=play', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.34a9f75c-f5e7-442a-743e-c9d9a11b70be?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (13, 'The Thing', 109, 1982, 0, 'A research team in Antarctica is hunted by a shape-shifting alien that assumes the appearance of its victims.', '/Images/Thumbnails/theThing.jpg');
INSERT INTO genres VALUES (13, 'horror', 'mystery', 'sci-fi');
INSERT INTO links VALUES (13, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.e2a9f72d-7882-9049-4311-e74d8360643a?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (14, 'The Exorcist', 122, 1973, 0, 'When a teenage girl is possessed by a mysterious entity, her mother seeks the help of two priests to save her daughter.', '/Images/Thumbnails/theExorcist.jpg');
INSERT INTO genres VALUES (14, 'horror', '', '');
INSERT INTO links VALUES (14, '', '', 'https://play.hbomax.com/page/urn:hbo:page:GXdu2RAwNtJuAuwEAADZy:type:feature?source=googleHBOMAX&action=play', '', '');

INSERT INTO movies VALUES (15, 'Hallowen', 91, 1978, 0, 'Fifteen years after murdering his sister on Halloween night 1963, Michael Myers escapes from a mental hospital and returns to the small town of Haddonfield, Illinois to kill again.', '/Images/Thumbnails/halloween.jpg');
INSERT INTO genres VALUES (15, 'horror', 'thriller', '');
INSERT INTO links VALUES (15, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.56a9f757-2dfe-6ba7-02c1-7e800e81f567?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (16, 'An American Werewolf in London', 97, 1981, 0, 'Two American college students on a walking tour of Britain are attacked by a werewolf that none of the locals will admit exists.', '/Images/Thumbnails/americanWerewolfLondon.jpg');
INSERT INTO genres VALUES (16, 'comedy', 'horror', '');
INSERT INTO links VALUES (16, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.9ccd394f-5cf3-4e72-8180-c39846acf7ff?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (17, 'Jaws', 124, 1975, 0, 'When a killer shark unleashes chaos on a beach community off Cape Cod, it\'s up to a local sheriff, a marine biologist, and an old seafarer to hunt the beast down.', '/Images/Thumbnails/jaws.jpg');
INSERT INTO genres VALUES (17, 'adventure', 'thriller', '');
INSERT INTO links VALUES (17, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.4ea9f78d-717c-d675-f5d5-2163986b8c3a?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (18, 'The Texas Chain Saw Massacre', 83, 1974, 0, 'Five friends head out to rural Texas to visit the grave of a grandfather. On the way they stumble across what appears to be a deserted house, only to discover something sinister within. Something armed with a chainsaw.', '/Images/Thumbnails/texasChainSawMassacre.jpg');
INSERT INTO genres VALUES (18, 'horror', '', '');
INSERT INTO links VALUES (18, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.86b00452-b02a-2552-a6f9-1bc3b4b62b38?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (19, 'Rosemary\'s Baby', 137, 1968, 0, 'A young couple trying for a baby moves into an aging, ornate apartment building on Central Park West, where they find themselves surrounded by peculiar neighbors.', '/Images/Thumbnails/rosemaryBaby.jpg');
INSERT INTO genres VALUES (19, 'drama', 'horror', '');
INSERT INTO links VALUES (19, '', 'https://www.hulu.com/movie/rosemarys-baby-84e3c555-f846-43cb-ae3f-54410d6ae6de?entity_id=84e3c555-f846-43cb-ae3f-54410d6ae6de', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.6eb6d631-54a4-3389-58d5-003fd1acc60d?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (20, 'The Blair Witch Project', 81, 1999, 0, 'Three film students vanish after traveling into a Maryland forest to film a documentary on the local Blair Witch legend, leaving only their footage behind.', '/Images/Thumbnails/blairWitchProject.jpg');
INSERT INTO genres VALUES (20, 'horror', 'mystery', '');
INSERT INTO links VALUES (20, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.22a9f70a-f220-60de-e28e-80a8b675e4fa?autoplay=0&ref_=atv_cf_strg_wb');

-- TOP 10 COMEDY MOVIES


INSERT INTO movies VALUES (21, 'Monty Python and the Holy Grail ', 91, 1975, 0, 'King Arthur and his Knights of the Round Table embark on a surreal, low-budget search for the Holy Grail, encountering many, very silly obstacles.', '/Images/Thumbnails/montyPythonandTheHolyGrail');
INSERT INTO genres VALUES (21, 'adventure', 'comedy', 'fantasy');
INSERT INTO links VALUES (21, 'https://www.netflix.com/watch/771476?source=35', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.60b4a834-49c5-4e48-c351-2c32e9d4fd5d?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (22, 'Airplane!', 88, 1980, 0, 'A man afraid to fly must ensure that a plane lands safely after the pilots become sick.', '/Images/Thumbnails/airplane');
INSERT INTO genres VALUES (22, 'comedy', '', '');
INSERT INTO links VALUES (22, '', 'https://www.hulu.com/watch/e97678f4-0244-4271-9815-ed913f0f1558', '', '', 'https://play.hbomax.com/feature/urn:hbo:feature:GYqyZQgahRT52wwEAAAAn?source=googleHBOMAX&action=play');

INSERT INTO movies VALUES (23, 'Blazing Saddles', 93, 1974, 0, 'In order to ruin a western town, a corrupt politician appoints a black Sheriff, who promptly becomes his most formidable adversary.', '/Images/Thumbnails/blazingSaddles');
INSERT INTO genres VALUES (23, 'comedy', 'western', '');
INSERT INTO links VALUES (23, '', 'https://www.hulu.com/watch/49b83348-04c7-47dd-b10d-fab2dfc3d640', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.5ea9f7b5-8b42-1e76-ab8d-cdd11ec1c2e3?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (24, 'Caddyshack', 98, 1980, 0, 'An exclusive golf course has to deal with a brash new member and a destructive dancing gopher.', '/Images/Thumbnails/caddyshack');
INSERT INTO genres VALUES (24, 'comedy', 'sport', '');
INSERT INTO links VALUES (24, '', '', 'https://play.hbomax.com/feature/urn:hbo:feature:GYGM9oAgwhazCwgEAAACX?source=googleHBOMAX&action=play', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.54a9f7b9-faa7-a57f-6055-6bff01c93a1f?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (25, 'Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb', 95, 1964, 0, 'An insane American general orders a bombing attack on the Soviet Union, triggering a path to nuclear holocaust that a war room full of politicians and generals frantically tries to stop.', '/Images/ThumbnailsdrStrangeorHILtSWaLtB/');
INSERT INTO genres VALUES (25, 'comedy', 'war', '');
INSERT INTO links VALUES (25, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.b8a9f727-ce30-2143-8171-c5e0ec2448e3?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (26, 'Duck Soup', 69, 1933, 0, 'Rufus T. Firefly is named the dictator of bankrupt Freedonia and declares war on neighboring Sylvania over the love of his wealthy backer Mrs. Teasdale, contending with two inept spies who cannot seem to keep straight which side they are on.', '/Images/Thumbnails/duckSoup');
INSERT INTO genres VALUES (26, 'comedy', 'musical', '');
INSERT INTO links VALUES (26, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.12a9f77c-ebe3-6f31-4bf9-0ba448f50fa3?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (27, 'Annie Hall', 93, 1977, 0, 'Alvy Singer, a divorced Jewish comedian, reflects on his relationship with ex-lover Annie Hall, an aspiring nightclub singer, which ended abruptly just like his previous marriages.', '/Images/Thumbnails/annieHall');
INSERT INTO genres VALUES (27, 'comedy', 'romance', '');
INSERT INTO links VALUES (27, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.60a9f727-0fe5-b302-1f62-a6df25e60250?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (28, 'Modern Times', 87, 1936, 0, 'The Tramp struggles to live in modern industrial society with the help of a young homeless woman.', '/Images/Thumbnails/modernTimes');
INSERT INTO genres VALUES (28, 'comedy', 'drama', 'romance');
INSERT INTO links VALUES (28, '', '', 'https://play.hbomax.com/feature/urn:hbo:feature:GXk3juwcBFTC3wwEAAAfD?source=googleHBOMAX&action=play', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.48a9f764-4d8b-7d58-dfa5-146d80b91028?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (29, 'The Jerk', 94, 1979, 0, 'A simpleminded, sheltered country boy suddenly decides to leave his family home to experience life in the big city, where his naivete is both his best friend and his worst enemy.', '/Images/Thumbnails/theJerk');
INSERT INTO genres VALUES (29, 'comedy', '', '');
INSERT INTO links VALUES (29, '', '', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.d8a9f7c2-48be-3607-4778-51304c61761d?autoplay=0&ref_=atv_cf_strg_wb');

INSERT INTO movies VALUES (30, 'Stir Crazy', 111, 1980, 0, 'Set up and wrongfully accused, two best friends will be sent to prison for a crime they did not commit. However, no prison cell could keep them locked in.', '/Images/Thumbnails/stirCrazy');
INSERT INTO genres VALUES (30, 'comedy', 'crime', '');
INSERT INTO links VALUES (30, '', 'https://www.hulu.com/watch/c53dd0dc-fc77-42de-b55d-157436d67bdd', '', '', 'https://www.amazon.com/gp/video/detail/amzn1.dv.gti.b4a9f773-08fb-f692-b3e4-a9ba5ca283f5?autoplay=0&ref_=atv_cf_strg_wb');
