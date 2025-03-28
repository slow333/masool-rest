package ma.sool.art;

import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.sool.hoguser.UserRepo;
import ma.sool.system.IdWorker;
import ma.sool.system.converter.ArtToDtoConverter;
import ma.sool.system.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtService{

  private final ArtRepo artRepo;
  private final IdWorker idWorker;
  private final ArtToDtoConverter artToDtoConverter;
  private final UserRepo userRepo;

  @Observed(name="artifact", contextualName = "findByIdService")
  public Art findById(String artId) {
    return artRepo.findById(artId).orElseThrow(() -> new ObjectNotFoundException("art", artId));
  }

  @Timed("findAllArtifactsService.time")
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

  public Page<Art> findAll(Pageable pageable) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.asc("name"));
    Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sorts));
    return artRepo.findAll(pageable1);
  }
}
