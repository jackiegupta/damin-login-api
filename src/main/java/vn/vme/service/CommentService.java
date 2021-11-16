package vn.vme.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.firebase.database.DatabaseReference;

import vn.vme.common.JConstants.Status;
import vn.vme.common.URI;
import vn.vme.entity.Comment;
import vn.vme.entity.User;
import vn.vme.io.notify.CommentRequest;
import vn.vme.io.notify.CommentVO;
import vn.vme.repository.CommentRepository;
import vn.vme.repository.UserRepository;

@Service
public class CommentService extends BaseService{
	private static final Logger log = LoggerFactory.getLogger(CommentService.class);

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;

	
	@Autowired
	Environment env;
	
	@Autowired
	public FirebaseService firebase;
	
	public CommentService() {
		
	}
	
	public Comment findOne(Long id) {
		return commentRepository.findById(id).get();
	}
	public CommentVO getDetail(Comment comment) {
		CommentVO commentVO = comment.getVO();
		if(isAdmin()) {
			//commentVO.setUser(userServiceClient.getUser(comment.getUserId()));
		}else {
			//commentVO.setUser(userServiceClient.getUserDO(comment.getUserId()));	
		}
		
		return commentVO;
	}
	
	public Page<Comment> findByUserId(Long userId, Pageable paging) {
		return commentRepository.findByUserId(userId,paging);
	}
	
	public Page<Comment> findAll(Pageable pageable) {
		return commentRepository.findAll(pageable);
	}
	
	public Comment save(Comment comment) {
		boolean isNew = comment.getId() == null || comment.getId()==0;
		log.debug("save " + isNew);
		comment = commentRepository.save(comment);
		
		if(comment.getStatus() != null && comment.getStatus().equals(Status.ACTIVE.name())) {
			this.updateFirebase(comment);
		}
		
		return comment;
	}

	public void delete(Long id) {
		commentRepository.deleteById(id);
	}
	
	public Comment create(CommentRequest request) {
		Comment comment = new Comment(request);
		comment.setStatus(request.getStatus());
		comment.setUserId(request.getUserId());
		comment.setCreateDate(new Date());
		comment.setUpdateDate(new Date());
		return this.save(comment);
	}
	
	/*
	public void initFirebase() {
		
		String profile = env.getActiveProfiles()[0];
		Long roomId = 1l;// room.getId();
		String firebasePath = profile + "/" + roomId;
		DatabaseReference fdb = firebase.db.getReference(firebasePath);

		Map<String, CommentVO> list = new HashMap<>();

		// Push initial commentList
		Page<Comment> result = this.findById(1);
		List<Comment> contentList = result.getContent();
		contentList.forEach(comment -> {
			CommentVO commentVO = this.getDetail(comment);
			list.put(String.valueOf(commentVO.getId()), commentVO);
		});
		fdb.child(URI.COMMENT).setValueAsync(list);
	}
	*/
	
	private Page<Comment> findById(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateFirebase(Comment comment) {

		String profile = env.getActiveProfiles()[0];
		Long userId = comment.getUserId();
		if(userId == 0) {//send all user
			Iterable<User> listUser = userRepository.findAll();
			Iterator<User> iter = listUser.iterator();
			while(iter.hasNext()){
				pushComment(comment, profile, iter.next().getId());
			}
		}else {
			pushComment(comment, profile, userId);
		}
		
		
	}

	private void pushComment(Comment comment, String profile, Long userId) {
		String firebasePath = profile + "/" + userId + URI.COMMENT;
		DatabaseReference fdb = firebase.db.getReference(firebasePath);
		Map<String, CommentVO> list = new HashMap<>();
		list.put(String.valueOf(comment.getId()), comment.getVO());
		fdb.push().setValueAsync(this.getDetail(comment));
	}
	
}
