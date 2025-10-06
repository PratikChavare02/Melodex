package in.melodexapi.repository;

import in.melodexapi.document.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumsRepository extends MongoRepository<Album,String> {
}
