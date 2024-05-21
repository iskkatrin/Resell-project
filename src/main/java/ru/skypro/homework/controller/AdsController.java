package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.service.AdsService;


import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {

    @Autowired
    private AdsService adsService;

    @GetMapping
    @Operation(summary = "Получение всех объявлений")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<List<Ad>> getAllAds() {
        List<Ad> ads = adsService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    @PostMapping
    @Operation(summary = "Добавление объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Ad> addAd(@RequestParam("properties") CreateOrUpdateAd properties,
                                    @RequestParam("image") MultipartFile image) {
        Ad ad = adsService.addAd(properties, image);
        return new ResponseEntity<>(ad, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable int id) {
        List<Comment> comments = adsService.getCommentsForAd(id);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        Comment newComment = adsService.addCommentToAd(id, comment);
        return ResponseEntity.ok(newComment);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ExtendedAd.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ExtendedAd> getAd(@PathVariable int id) {
        ExtendedAd ad = adsService.getAdById(id);
        return ResponseEntity.ok(ad);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Void> removeAd(@PathVariable int id) {
        adsService.removeAd(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Ad.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
                    })
            public ResponseEntity<Ad>updateAd(@PathVariable int id, @RequestBody CreateOrUpdateAd ad) {
        Ad updatedAd = adsService.updateAd(id, ad);
        return ResponseEntity.ok(updatedAd);
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Void> updateImage(@PathVariable int id, @RequestParam("image") MultipartFile image) {
        adsService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<Ad>> getAdsMe() {
        List<Ad> ads = adsService.getAdsForLoggedInUser();
        return ResponseEntity.ok(ads);
    }
}