package com.insta.instagram.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Post;
import com.insta.instagram.modal.User;
import com.insta.instagram.response.MessageResponse;
import com.insta.instagram.service.PostService;
import com.insta.instagram.service.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<Post> createPostHandler(@RequestBody Post post,@RequestHeader("Authorization") String token) throws UserException {
		
		User user = userService.findUserProfile(token);
		Post createdPost = postService.createPost(post, user.getId());
		
		return new ResponseEntity<Post>(createdPost,HttpStatus.OK);
	}
	
	@GetMapping("/all/{userId}")
	public ResponseEntity<List<Post>> findPostByUserIdHandler(@PathVariable(name="userId") Integer userIdParameter) throws UserException {
		
		List<Post> posts = postService.findPostByUserId(userIdParameter);
		
		return new ResponseEntity<List<Post>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/following/{userIds}")
	public ResponseEntity<List<Post>> findAllPostByUserIdsHandler(@PathVariable(name="userIds") List<Integer> userIdsParameter) throws PostException, UserException {
		
		List<Post> posts = postService.findAllPostByUserIds(userIdsParameter);
		
		return new ResponseEntity<List<Post>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<Post> findPostByIdHandler(@PathVariable(name="postId") Integer postIdParameter) throws PostException {
		
		Post post = postService.findPostById(postIdParameter);
		
		return new ResponseEntity<Post>(post,HttpStatus.OK);
	}
	
	@PutMapping("/like/{postId}")
	public ResponseEntity<Post> likePostHandler(@PathVariable(name="postId") Integer postIdParameter,@RequestHeader("Authorization") String token) throws UserException, PostException {
		
		User user = userService.findUserProfile(token);
		
		Post likedPost = postService.likePost(postIdParameter, user.getId());
		
		return new ResponseEntity<Post>(likedPost,HttpStatus.OK);
	}
	
	@PutMapping("/unlike/{postId}")
	public ResponseEntity<Post> unlikePostHandler(@PathVariable(name="postId") Integer postIdParameter,@RequestHeader("Authorization") String token) throws UserException, PostException {
		
		User user = userService.findUserProfile(token);
		
		Post likedPost = postService.unLikePost(postIdParameter, user.getId());
		
		return new ResponseEntity<Post>(likedPost,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{postId}")
	public ResponseEntity<MessageResponse> deletePostHandler(@PathVariable(name="postId") Integer postIdParameter,@RequestHeader("Authorization") String token) throws UserException, PostException {
		
		User user = userService.findUserProfile(token);
		
		String message = postService.deletePost(postIdParameter, user.getId());
		
		MessageResponse res = new MessageResponse(message);
		
		return new ResponseEntity<MessageResponse>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/save_post/{postId}")
	public ResponseEntity<MessageResponse> savedPostHandler(@PathVariable(name="postId") Integer postIdParameter,@RequestHeader("Authorization") String token) throws UserException, PostException {
		
		User user = userService.findUserProfile(token);
		
		String message = postService.savedPost(postIdParameter, user.getId());
		
		MessageResponse res = new MessageResponse(message);
		
		return new ResponseEntity<MessageResponse>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/unsave_post/{postId}")
	public ResponseEntity<MessageResponse> unsavedPostHandler(@PathVariable(name="postId") Integer postIdParameter,@RequestHeader("Authorization") String token) throws UserException, PostException {
		
		User user = userService.findUserProfile(token);
		
		String message = postService.unSavedPost(postIdParameter, user.getId());
		
		MessageResponse res = new MessageResponse(message);
		
		return new ResponseEntity<MessageResponse>(res,HttpStatus.ACCEPTED);
	}
}
