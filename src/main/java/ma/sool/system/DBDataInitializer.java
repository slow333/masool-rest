package ma.sool.system;

import lombok.RequiredArgsConstructor;
import ma.sool.art.Art;
import ma.sool.art.ArtRepo;
import ma.sool.hoguser.HogUser;
import ma.sool.hoguser.UserRepo;
import ma.sool.wiz.Wiz;
import ma.sool.wiz.WizRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DBDataInitializer implements CommandLineRunner {

  private final ArtRepo artRepo;
  private final WizRepo wizRepo;
  private final UserRepo userRepo;

  @Override
  public void run(String... args) throws Exception {
    Art a1 = new Art();
    a1.setId("1250808601744904191");
    a1.setName("Deluminator");
    a1.setDescription("A Deluminator is a device invented by Albus Dumbledore ...");
    a1.setImgUrl("ImageUrl");

    Art a2 = new Art();
    a2.setId("1250808601744904192");
    a2.setName("Invisibility Cloak");
    a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
    a2.setImgUrl("ImageUrl");

    Art a3 = new Art();
    a3.setId("1250808601744904193");
    a3.setName("Elder Wand");
    a3.setDescription("The Elder Wand, known throughout history as the Deathstick ...");
    a3.setImgUrl("ImageUrl");

    Art a4 = new Art();
    a4.setId("1250808601744904194");
    a4.setName("The Marauder's Map");
    a4.setDescription("A magical map of Hogwarts created by Remus Lupin, ...");
    a4.setImgUrl("ImageUrl");

    Art a5 = new Art();
    a5.setId("1250808601744904195");
    a5.setName("The Sword Of Gryffindor");
    a5.setDescription("A goblin-made sword adorned with large rubies on the pommel....");
    a5.setImgUrl("ImageUrl");

    Art a6 = new Art();
    a6.setId("1250808601744904196");
    a6.setName("Resurrection Stone");
    a6.setDescription("The Resurrection Stone allows the holder to bring back ...");
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

    HogUser u1 = new HogUser();
    u1.setId(1L);
    u1.setUsername("kim");
    u1.setPassword("123");
    u1.setEnabled(true);
    u1.setRoles("admin user");

    HogUser u2 = new HogUser();
    u2.setId(2L);
    u2.setUsername("woo");
    u2.setPassword("321");
    u2.setEnabled(true);
    u2.setRoles("user");

    HogUser u3 = new HogUser();
    u3.setId(3L);
    u3.setUsername("jin");
    u3.setPassword("321");
    u3.setEnabled(true);
    u3.setRoles("user");

    HogUser u4 = new HogUser();
    u4.setId(4L);
    u4.setUsername("dong");
    u4.setPassword("321");
    u4.setEnabled(false);
    u4.setRoles("user");

    wizRepo.save(w1);
    wizRepo.save(w2);
    wizRepo.save(w3);

    artRepo.save(a6);
    userRepo.save(u1);
    userRepo.save(u2);
    userRepo.save(u3);
    userRepo.save(u4);
  }
}
