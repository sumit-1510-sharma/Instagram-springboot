package com.insta.instagram.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insta.instagram.exceptions.StoryException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Story;
import com.insta.instagram.modal.User;
import com.insta.instagram.service.StoryService;
import com.insta.instagram.service.UserService;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

	@Autowired
	private StoryService storyService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<Story> createStoryHandler(@RequestBody Story story, @RequestHeader("Authorization") String token) throws UserException {
		
		User user = userService.findUserProfile(token);
		
		Story createdStory = storyService.createStory(story, user.getId());
		
		return new ResponseEntity<Story>(createdStory,HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<List<Story>> findAllStoryByUserIdHandler(@PathVariable(name="userId") Integer userIdParameter) throws UserException, StoryException {
		
		List<Story> stories = storyService.findStoryByUserId(userIdParameter);
		
		return new ResponseEntity<List<Story>>(stories,HttpStatus.OK);
	}
}
