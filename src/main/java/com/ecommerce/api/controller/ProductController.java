package com.ecommerce.api.controller;

import com.ecommerce.api.dtos.ProductRequestDto;
import com.ecommerce.api.dtos.ProductResponseDto;
import com.ecommerce.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Tag(name = "Product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Busca todos os produtos")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada")
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> listAll() {
        return ResponseEntity.ok(productService.findAllProduct());
    }

    @Operation(summary = "Busca um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Produto não encontrado com o ID fornecido\"}")
                    )
            )
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @Operation(summary = "Busca produtos por categoria", description = "Retorna uma lista de produtos pertencentes a uma categoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados."),
            @ApiResponse(responseCode = "404", description = "Categoria inexsitente.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Categoria inexistente\"}")
                    )
            )
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> findByCategory(@PathVariable String category) {
        List<ProductResponseDto> products = productService.findProductByCategory(category);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Cria um novo produto", description = "Requer token JWT com permissões administrativas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na validação ou nome duplicado",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Nome Duplicado", value = "{ \"message\": \"Já existe um produto com este nome\"}"),
                                    @ExampleObject(name = "Dados Inválidos", value = "{ \"message\": \"O preço deve ser maior que zero\"}")
                            }
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Token ausente ou expirado\"}")
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productDto) {
        return ResponseEntity.status(201).body(productService.createProduct(productDto));
    }

    @Operation(summary = "Atualiza um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Produto não existe para atualização\"}")
                    )
            )
    })
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID productId, @Valid @RequestBody ProductRequestDto product) {
        return ResponseEntity.ok(productService.updatingProduct(productId, product));
    }

    @Operation(summary = "Deleta um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado para exclusão",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Erro ao deletar: ID inválido\"}")
                    )
            )
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }
}