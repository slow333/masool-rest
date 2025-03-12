package ma.sool.system;

import lombok.RequiredArgsConstructor;
import ma.sool.art.Art;
import ma.sool.art.ArtRepo;
import ma.sool.wiz.Wiz;
import ma.sool.wiz.WizRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBDataInitializer implements CommandLineRunner {

  private final ArtRepo artRepo;
  private final WizRepo wizRepo;

  @Override
  public void run(String... args) throws Exception {
    Art a1 = new Art();
    a1.setId("1250808601744904191");
    a1.setName("Deluminator");
    a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
    a1.setImgUrl("ImageUrl");

    Art a2 = new Art();
    a2.setId("1250808601744904192");
    a2.setName("Invisibility Cloak");
    a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
    a2.setImgUrl("ImageUrl");

    Art a3 = new Art();
    a3.setId("1250808601744904193");
    a3.setName("Elder Wand");
    a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
    a3.setImgUrl("ImageUrl");

    Art a4 = new Art();
    a4.setId("1250808601744904194");
    a4.setName("The Marauder's Map");
    a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
    a4.setImgUrl("ImageUrl");

    Art a5 = new Art();
    a5.setId("1250808601744904195");
    a5.setName("The Sword Of Gryffindor");
    a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
    a5.setImgUrl("ImageUrl");

    Art a6 = new Art();
    a6.setId("1250808601744904196");
    a6.setName("Resurrection Stone");
    a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
    a6.setImgUrl("ImageUrl");

    Wiz w1 = new Wiz();
    w1.setId(1);
    w1.setName("Albus Dumbledore");
    w1.addArt(a1);
    w1.addArt(a3);

    Wiz w2 = new Wiz();
    w2.setId(2);
    w2.setName("Harry Potter");
    w2.addArt(a2);
    w2.addArt(a4);

    Wiz w3 = new Wiz();
    w3.setId(3);
    w3.setName("Neville Longbottom");
    w3.addArt(a5);

    wizRepo.save(w1);
    wizRepo.save(w2);
    wizRepo.save(w3);

    artRepo.save(a6);
  }
}
