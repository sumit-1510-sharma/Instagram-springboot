package com.insta.instagram.service;

import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Comment;

public interface CommentService {

	public Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException, PostException;
	
	public Comment findCommentById(Integer commentId) throws CommentException;
	
	public Comment likeComment(Integer commentId, Integer userId) throws CommentException, UserException;
	
	public Comment unlikeComment(Integer commentId, Integer userId) throws CommentException, UserException;
}
