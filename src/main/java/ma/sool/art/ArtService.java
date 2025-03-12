package ma.sool.art;

import jakarta.transaction.Transactional;
import ma.sool.system.IdWorker;
import ma.sool.system.converter.ArtToDtoConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtService {

  private final ArtRepo artRepo;
  private final IdWorker idWorker;
  private final ArtToDtoConverter artToDtoConverter;

  public ArtService(ArtRepo artRepo, IdWorker idWorker, ArtToDtoConverter artToDtoConverter) {
    this.artRepo = artRepo;
    this.idWorker = idWorker;
    this.artToDtoConverter = artToDtoConverter;
  }

  public Art findById(String artId) {
    return artRepo.findById(artId).orElseThrow(() -> new ArtNotFoundException(artId));
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
    }).orElseThrow(() -> new ArtNotFoundException(artId));
  }
}
