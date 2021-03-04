package com.example.demo.repositories;

import com.example.demo.entities.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Integer> {

}