package com.example.doan_tatruong.controller;

import com.example.doan_tatruong.dto.CategoryDto;
import com.example.doan_tatruong.dto.ProductDto;
import com.example.doan_tatruong.model.Category;
import com.example.doan_tatruong.model.Product;
import com.example.doan_tatruong.service.CategoryService;
import com.example.doan_tatruong.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductHomeController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menu(Model model){
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<ProductDto> productDtos = productService.products();
        model.addAttribute("categories",categories);
        model.addAttribute("products",productDtos);
        return "index";
    }
    @RequestMapping(value = {"/", "index"}, method = RequestMethod.GET)
    public String products(Model model){
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        List<ProductDto> products = productService.randomProduct();
        List<Product> listViewProduct = productService.listViewProduct();
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("viewProduct", listViewProduct);
        model.addAttribute("products", products);
        return "shop";
    }
    @RequestMapping(value = "/find-product/{id}", method = RequestMethod.GET)
    public String findProduct(@PathVariable("id") Long id, Model model){
        Product product = productService.getProductById(id);
        Long categoryId = product.getCategory().getId();
        List<Product> products = productService.getRelacedProducts(categoryId);
        model.addAttribute("product", product);
        model.addAttribute("products", products);
        return "product-detail";
    }
    @RequestMapping(value = "/products-in-category/{id}",method = RequestMethod.GET)
    public String getProductsInCatrgory(@PathVariable("id") Long categoryId, Model model){
        Category category = categoryService.findById(categoryId);
        List<CategoryDto> categories = categoryService.getCategoryAndProduct();
        List<Product> products = productService.getProductsInCategory(categoryId);
        model.addAttribute("categories", categories);
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "products-in-category";
    }
    @RequestMapping(value = "high-price", method = RequestMethod.GET)
    public String filterHighPrice(Model model){

        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        List<ProductDto> products = productService.filterHighPrice();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("products", products);
        return "shop";
    }
    @RequestMapping(value = "low-price", method = RequestMethod.GET)
    public String filterLowPrice(Model model){
        List<ProductDto> listView = productService.listViewProducts();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        List<ProductDto> products = productService.filterLowPrice();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("products", products);
        return "shop";
    }
    @RequestMapping(value = "/search-product", method = RequestMethod.GET)
     public String searchProducts(@RequestParam("keyword") String keyword, Model model){
       List<CategoryDto> categoryDtos = categoryService.getCategoryAndProduct();
       List<ProductDto> productDtos = productService.searchProducts(keyword);
       List<ProductDto> listView = productService.listViewProducts();
       model.addAttribute("viewProduct", listView);
       model.addAttribute("categories", categoryDtos);
       model.addAttribute("title", "Search Products");
       model.addAttribute("page", "Result Search");
       model.addAttribute("products", productDtos);
       return "shop";
   }
   }

