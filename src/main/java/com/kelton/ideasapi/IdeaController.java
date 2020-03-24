package com.kelton.ideasapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ideas")
public class IdeaController {

	private IdeaRepository repo;
	
	public IdeaController(IdeaRepository repo) {
		this.repo = repo;
	}
	
	@GetMapping
	public ResponseEntity<List<ColoredIdea>> getIdeas(){
		
		List<Idea> ideas = repo.findAll();
		
		if(ideas.isEmpty()) { 
			return ResponseEntity.noContent().build();
		} else { 
			List<ColoredIdea> coloredIdeas = new ArrayList<>();
			ideas.forEach( idea -> {
				Random random = new Random();
				int nextInt = random.nextInt(256*256*256);
				String hexColor = String.format("#%06x", nextInt);
				
				var coloredIdea =  new ColoredIdea(idea.getId(),idea.getDescription(), idea.getStatus(), hexColor);
				coloredIdeas.add(coloredIdea);
			});
			return ResponseEntity.ok().body(coloredIdeas);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Idea> getIdea(@PathVariable Long id){
		return repo.findById(id)
				.map(idea -> { return ResponseEntity.ok(idea); })
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<Idea> addIdea(@RequestBody String description){
		if(!repo.findByDescription(description).isEmpty()) 
			return ResponseEntity.badRequest().build();

		var idea = new Idea(description, Status.PENDING);
		return ResponseEntity.ok(repo.save(idea));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Idea> updateIdea(@PathVariable Long id, @RequestBody Status status){
		return repo.findById(id)
				.map(idea -> {
					idea.setStatus(status);
					return ResponseEntity.ok(repo.save(idea));
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteIdea(@PathVariable Long id){
		return repo.findById(id)
				.map(idea -> {
					repo.deleteById(id);
					return ResponseEntity.noContent().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
}
