package org.unibl.eurobasket.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.eurobasket.model.Comment;
import org.unibl.eurobasket.model.News;
import org.unibl.eurobasket.model.User;
import org.unibl.eurobasket.repository.CommentRepository;
import org.unibl.eurobasket.repository.NewsRepository;

import java.util.Optional;

@Service
public class NewsService {

    private NewsRepository newsRepository;
    private CommentRepository commentRepository;

    public JSONObject getAllNews() {
        JSONObject result = new JSONObject();
        JSONArray newsArray = new JSONArray();
        newsRepository.findAll().forEach(news -> newsArray.put(createNewsJsonResponse(news)));
        result.put("news", newsArray);
        return result;
    }

    public JSONObject createNews(String title, String content, User currentUser) {
        News news = new News(currentUser, title, content);
        newsRepository.save(news);
        return createNewsJsonResponse(news);
    }

    public JSONObject addComment(int id, String text, User currentUser) {
        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            Comment comment = new Comment(currentUser, news, text);
            commentRepository.save(comment);
            return createNewsJsonResponse(news);
        } else {
            return null;
        }
    }

    public JSONObject deleteNewsById(int id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            JSONObject response = new JSONObject();
            response.put("deleted news", createNewsJsonResponse(news.get()));
            for (Comment comment : commentRepository.findAllByNews(news.get())) {
                commentRepository.delete(comment);
            }
            newsRepository.delete(news.get());
            return response;
        }
        return null;
    }

    public JSONObject deleteCommentById(int id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            JSONObject response = new JSONObject();
            response.put("deleted news", createCommentJson(comment.get()));
            commentRepository.delete(comment.get());
            return response;
        }
        return null;
    }

    private JSONObject createNewsJsonResponse(News news) {
        JSONObject response = new JSONObject();
        response.put("id", news.getId());
        response.put("creator", news.getCreator().getUsername());
        response.put("title", news.getTitle());
        response.put("content", news.getContent());
        response.put("comments", createCommentJsonFromNews(news));
        return response;
    }

    private JSONArray createCommentJsonFromNews(News news) {
        JSONArray comments = new JSONArray();
        for (Comment comment : commentRepository.findAllByNews(news)) {
            comments.put(createCommentJson(comment));
        }
        return comments;
    }

    private JSONObject createCommentJson(Comment comment) {
        JSONObject commentJson = new JSONObject();
        commentJson.put("user", comment.getCreator().getUsername());
        commentJson.put("comment", comment.getText());
        commentJson.put("id", comment.getId());
        return commentJson;
    }

    @Autowired
    public void setNewsRepository(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
