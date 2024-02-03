package com.insta.instagram.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Comment;
import com.insta.instagram.modal.Post;
import com.insta.instagram.modal.User;
import com.insta.instagram.repository.CommentRepository;
import com.insta.instagram.repository.PostRepository;

@Service
public class CommentServiceImplementation implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostRepository postRepository;
	
	@Override
	public Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException, PostException {
		
		User user = userService.findUserById(userId);
		
		Post post = postService.findPostById(postId);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		comment.setUser(userDto);
		comment.setCreatedAt(LocalDateTime.now());
		
		Comment createdComment = commentRepository.save(comment);
		post.getComments().add(createdComment);
		
		postRepository.save(post);
		
		return createdComment;
	}

	@Override
	public Comment findCommentById(Integer commentId) throws CommentException {
		
		Optional<Comment> opt = commentRepository.findById(commentId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		
		throw new CommentException("Comment does not exist with id: " + commentId);
	}

	@Override
	public Comment likeComment(Integer commentId, Integer userId) throws CommentException, UserException {
		
		User user = userService.findUserById(userId);
		Comment comment = findCommentById(commentId);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		comment.getLikedByUsers().add(userDto);
		
		commentRepository.save(comment);
		
		//return commentRepository.save(comment);
		return comment;
	}

	@Override
	public Comment unlikeComment(Integer commentId, Integer userId) throws CommentException, UserException {
		
		User user = userService.findUserById(userId);
		Comment comment = findCommentById(commentId);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		comment.getLikedByUsers().remove(userDto);
		
		commentRepository.save(comment);
		
		//return commentRepository.save(comment);
		return comment;
	}

	
}
