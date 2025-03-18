package ma.sool.art;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.sool.hoguser.MyUserPrincipal;
import ma.sool.hoguser.UserRepo;
import ma.sool.system.IdWorker;
import ma.sool.system.converter.ArtToDtoConverter;
import ma.sool.system.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtService{

  private final ArtRepo artRepo;
  private final IdWorker idWorker;
  private final ArtToDtoConverter artToDtoConverter;
  private final UserRepo userRepo;
  
  public Art findById(String artId) {
    return artRepo.findById(artId).orElseThrow(() -> new ObjectNotFoundException("art", artId));
  }

  public List<Art> findAll() {
    return artRepo.findAll();
  }

  public Art createArt(Art art) {
    art.setId(idWorker.nextId() + "");
    Art savedArt = artRepo.save(art);
    return savedArt;
  }

  public Art updateArt(String artId, Art newArt) {
    return artRepo.findById(artId).map(oldArt -> {
      oldArt.setName(newArt.getName());
      oldArt.setDescription(newArt.getDescription());
      oldArt.setImgUrl(newArt.getImgUrl());
      return artRepo.save(oldArt);
    }).orElseThrow(() -> new ObjectNotFoundException("art", artId));
  }

  public void deleteArt(String artId) {
    Art art = artRepo.findById(artId).orElseThrow(() -> new ObjectNotFoundException("art", artId));
//    art.getOwner().removeArt(art);
    artRepo.deleteById(artId);
  }

}
