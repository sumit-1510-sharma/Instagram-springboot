package com.insta.instagram.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Post;
import com.insta.instagram.modal.User;
import com.insta.instagram.repository.PostRepository;
import com.insta.instagram.repository.UserRepository;

@Service
public class PostServiceImplementation implements PostService {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Post createPost(Post post, Integer userId) throws UserException {
		
		User user = userService.findUserById(userId);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		post.setUser(userDto);
		Post createdPost = postRepository.save(post);
		
		return createdPost;
	}

	@Override
	public String deletePost(Integer postId, Integer userId) throws UserException, PostException {
		
		Post post = findPostById(postId);
		
		User user = userService.findUserById(userId);
		
		if(post.getUser().getId().equals(user.getId())) {
			postRepository.deleteById(post.getId());
			return "Post deleted successfully";
		}
		
		throw new PostException("You don't have access to delete this post");		
	}

	@Override
	public List<Post> findPostByUserId(Integer userId) throws UserException {
		
		List<Post> posts = postRepository.findByUserId(userId);
		
		if(posts.size()==0) {
			throw new UserException("This user does not have any posts");
		}
		
		return posts;
	}

	@Override
	public Post findPostById(Integer postId) throws PostException {
		
		Optional<Post> opt = postRepository.findById(postId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		
		throw new PostException("Post not found with id " + postId);
	}

	@Override
	public List<Post> findAllPostByUserIds(List<Integer> userIds) throws PostException, UserException {
		
		List<Post> posts = postRepository.findAllPostByUserIds(userIds);
		
		if(posts.size()==0) {
			throw new PostException("No posts found");
		}
		
		return posts;
	}

	@Override
	public String savedPost(Integer postId, Integer userId) throws PostException, UserException {
		
		Post post = findPostById(postId);
		
		User user = userService.findUserById(userId);
		
		if(!user.getSavedPost().contains(post)) {
			user.getSavedPost().add(post);
			userRepository.save(user);
		}
		
		return "Post saved successfully";
	}

	@Override
	public String unSavedPost(Integer postId, Integer userId) throws PostException, UserException {
		
		Post post = findPostById(postId);
		
		User user = userService.findUserById(userId);
		
		if(user.getSavedPost().contains(post)) {
			user.getSavedPost().remove(post);
			userRepository.save(user);
		}
		
		return "Post removed successfully";
	}

	@Override
	public Post likePost(Integer PostId, Integer userId) throws UserException, PostException {
		
		Post post = findPostById(PostId);
		
		User user = userService.findUserById(userId);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		post.getLikedByUsers().add(userDto);
		
		return postRepository.save(post);
	}

	@Override
	public Post unLikePost(Integer PostId, Integer userId) throws UserException, PostException {
		
		Post post = findPostById(PostId);
		
		User user = userService.findUserById(userId);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());
		
		post.getLikedByUsers().remove(userDto);
		
		return postRepository.save(post);
	}

}
