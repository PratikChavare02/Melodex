package in.melodexapi.repository;

import in.melodexapi.document.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SongRepository extends MongoRepository<Song,String> {
    Optional<Song> findByName(String name);

}
