package com.microservicios.item.models.service;

import com.microservicios.item.models.Item;
import com.springboot.app.commons.models.entity.Producto;

import java.util.List;

public interface ItemsService {

    public List<Item> findAll();
    public Item findById(Long id, Integer cantidad);
    
    public Producto save(Producto producto);
    
    public Producto update(Producto producto, Long id);
    
    public void delete(Long id);
}
