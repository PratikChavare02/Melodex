package in.melodexapi.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import in.melodexapi.document.Album;
import in.melodexapi.dto.AlbumListResponse;
import in.melodexapi.dto.AlbumRequest;
import in.melodexapi.repository.AlbumsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
// by using service object will automatically created for service class
public class AlbumService {
    private final AlbumsRepository albumsRepository;
    private final Cloudinary cloudinary;
    public Album addAlbum(AlbumRequest albumRequest) throws IOException {
        Map<String,Object> imageUploadResult= cloudinary.uploader().upload(albumRequest.getImageFile().getBytes(), ObjectUtils.asMap("resource_type","image"));
        Album newAlbum=Album.builder()
                .name(albumRequest.getName())
                .desc(albumRequest.getDesc())
                .bgColor(albumRequest.getBgColor())
                .imageUrl(imageUploadResult.get("secure_url").toString())
                .build();
        return albumsRepository.save(newAlbum);
    }

    //finding all albums
    public AlbumListResponse getAllAlbums(){
        return new AlbumListResponse(true,albumsRepository.findAll());

    }

    // remove album
    public Boolean removeAlbum(String id){
        Album existingAlbum=albumsRepository.findById(id).orElseThrow(()->new RuntimeException("Album not found"));

        albumsRepository.delete(existingAlbum);
        return  true;
    }

}
