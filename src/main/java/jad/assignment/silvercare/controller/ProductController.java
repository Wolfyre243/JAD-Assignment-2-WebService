package jad.assignment.silvercare.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jad.assignment.silvercare.model.Product;
import jad.assignment.silvercare.model.ProductDAO;


@RestController
@RequestMapping("/services")

public class ProductController {
  // GET /services
  @RequestMapping(method=RequestMethod.GET)
  public ArrayList<Product> getAllProducts(@RequestParam(required=false) String search) {
    ArrayList<Product> productsArr = new ArrayList<>();
    try {
      productsArr = ProductDAO.getAllProducts(search);
    } catch (Exception e) {
      System.out.print("Get Products Error:" + e);
    }

    return productsArr;
  }

  // GET /services/{productId}
  @RequestMapping(path="/{productId}", method=RequestMethod.GET)
  public Product getProductById(@PathVariable int productId) {
    Product productBean = null;
    try {
      productBean = ProductDAO.getProductById(productId);
    } catch (Exception e) {
      System.out.print("Get Product By Id Error:" + e);
    }

    return productBean;
  }
}
