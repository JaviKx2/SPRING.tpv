package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import daos.core.ArticleDao;
import entities.core.Article;
import wrappers.ArticleCreationWrapper;
import wrappers.ArticleUpdateWrapper;
import wrappers.ArticleWrapper;

@Controller
public class ArticleController {

    private ArticleDao articleDao;

    @Autowired
    public void setArticleDao(ArticleDao productDao) {
        this.articleDao = productDao;
    }

    public boolean articleCodeExists(String articleCode) {
        Article article = articleDao.findOne(articleCode);
        return article != null;
    }

    public Article getArticleByCode(String articleCode) {
        return articleDao.findOne(articleCode);
    }

    public boolean hasEnoughStock(String articleCode, int amount) {
        Article article = articleDao.findOne(articleCode);
        boolean enoughStock = false;
        if (article != null) {
            if (article.getStock() >= amount) {
                enoughStock = true;
            }
        }
        return enoughStock;
    }

    public void consumeArticle(String articleCode, int amount) {
        Article article = articleDao.findOne(articleCode);
        if (article != null) {
            int stock = article.getStock();
            article.setStock(stock - amount);
            articleDao.save(article);
        }
    }

    public void createArticle(ArticleCreationWrapper articleCreationWrapper) {
        Article articleToBeSaved = new Article();
        articleToBeSaved.setCode(articleCreationWrapper.getCode());
        articleToBeSaved.setReference(articleCreationWrapper.getReference());
        articleToBeSaved.setDescription(articleCreationWrapper.getDescription());
        articleToBeSaved.setDiscontinued(articleCreationWrapper.isDiscontinued());
        articleToBeSaved.setImage(articleCreationWrapper.getImage());
        articleToBeSaved.setRetailPrice(articleCreationWrapper.getRetailPrice());   
        articleToBeSaved.setWholesalePrice(articleCreationWrapper.getWholesalePrice());
        articleToBeSaved.setStock(articleCreationWrapper.getStock());
        articleDao.saveAndFlush(articleToBeSaved);   
    }

    public boolean articleExists(String code) {
        return articleDao.exists(code);
    }

    public void updateArticle(ArticleUpdateWrapper articleUpdateWrapper) {
        Article articleToBeUpdated = articleDao.findOne(articleUpdateWrapper.getCode());
        articleToBeUpdated.setCode(articleUpdateWrapper.getCode());
        articleToBeUpdated.setDescription(articleUpdateWrapper.getDescription());
        articleToBeUpdated.setReference(articleUpdateWrapper.getReference());
        articleToBeUpdated.setDiscontinued(articleUpdateWrapper.isDiscontinued());
        articleToBeUpdated.setImage(articleUpdateWrapper.getImage());
        articleToBeUpdated.setRetailPrice(articleUpdateWrapper.getRetailPrice());   
        articleToBeUpdated.setWholesalePrice(articleUpdateWrapper.getWholesalePrice());
        articleToBeUpdated.setStock(articleUpdateWrapper.getStock());
        articleDao.saveAndFlush(articleToBeUpdated);       
    }

    public List<ArticleWrapper> findAllArticles() {
        List<ArticleWrapper> articleWrappers = new ArrayList<>();
        for (Article article : articleDao.findAll()) {
            articleWrappers.add(entityToWrapper(article));
        }
        return articleWrappers;
    }

    public ArticleWrapper findOneArticle(String code) {
        return entityToWrapper(articleDao.findOne(code));
    }

    private ArticleWrapper entityToWrapper(Article article) {
        ArticleWrapper articleWrapper = new ArticleWrapper();
        articleWrapper.setCode(article.getCode());
        articleWrapper.setReference(article.getReference());
        articleWrapper.setDescription(article.getDescription());
        articleWrapper.setDiscontinued(article.isDiscontinued());
        articleWrapper.setImage(article.getImage());
        articleWrapper.setRetailPrice(article.getRetailPrice());
        articleWrapper.setWholesalePrice(article.getWholesalePrice());
        articleWrapper.setStock(article.getStock());
        return articleWrapper;
    }

    public void deleteArticle(String code) {
        articleDao.delete(code);      
    }
}
