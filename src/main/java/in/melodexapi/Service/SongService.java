package in.melodexapi.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import in.melodexapi.document.Song;
import in.melodexapi.dto.SongListResponse;
import in.melodexapi.dto.SongRequest;
import in.melodexapi.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final Cloudinary cloudinary;

    public Song addSong(SongRequest songRequest) throws IOException {
        Map<String,Object>audioUploadResult=cloudinary.uploader().upload(songRequest.getAudioFile().getBytes(), ObjectUtils.asMap("resource_type","video"));
        Map<String,Object>imageUploadResult=cloudinary.uploader().upload(songRequest.getImageFile().getBytes(), ObjectUtils.asMap("resource_type","image"));

        Double durationSecond=(Double)audioUploadResult.get("duration");
        String duration =formatDuration(durationSecond);

        //creating song object
        Song newsong=Song.builder()
                .name(songRequest.getName())
                .desc(songRequest.getDesc())
                .album(songRequest.getAlbum())
                .image(imageUploadResult.get("secure_url").toString())
                .file(audioUploadResult.get("secure_url").toString())
                .duration(duration)
                .build();
        return songRepository.save(newsong);


    }
    private String formatDuration(Double durationSeconds){
        if(durationSeconds==null){
            return "0.00";
        }
        int minutes=(int)(durationSeconds/60);
        int seconds=(int)(durationSeconds%60);
        return  String.format("%d:%02d", minutes,seconds);
    }

    //find all songs
    public SongListResponse getAllSongs(){
        return new SongListResponse(true,songRepository.findAll());
    }

    //delete song by id
    public boolean deletSong(String id){
        Song existingsong= songRepository.findById(id).orElseThrow(()->new RuntimeException("Song not found"));
        songRepository.delete(existingsong);
        return true;
    }

    //find song by name
    public Song getSongByName(String name){
        Song existing =songRepository.findByName(name).orElseThrow(()->new RuntimeException("Song is not Available"));
        return existing;

    }

}













