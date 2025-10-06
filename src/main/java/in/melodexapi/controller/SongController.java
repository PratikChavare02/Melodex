package in.melodexapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import in.melodexapi.Service.SongService;
import in.melodexapi.dto.SongRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @PostMapping
    public ResponseEntity<?> addSong(@RequestPart("request") String request,
                                     @RequestPart("audio") MultipartFile audioFile,
                                     @RequestPart("image") MultipartFile imageFile){
        try {
            ObjectMapper objectMapper =new ObjectMapper();
            SongRequest songRequest=objectMapper.readValue(request, SongRequest.class);
            songRequest.setImageFile(imageFile);
            songRequest.setAudioFile(audioFile);

            songService.addSong(songRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(songService.addSong(songRequest));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping
    public ResponseEntity<?> getAll(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(songService.getAllSongs());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSongById(@PathVariable String id){
        try {
            boolean remove =songService.deletSong(id);
            if(remove){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            }else{
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //get song by name;
    @GetMapping("/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name){
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(songService.getSongByName(name));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
