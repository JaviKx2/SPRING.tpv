package api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import api.exceptions.NotFoundProductCodeException;
import controllers.ProductController;
import entities.core.Product;
import wrappers.PatchChangeDescriptionWrapper;
import wrappers.ProductBarcodeWrapper;
import wrappers.ProductWrapper;

@RestController
@RequestMapping(Uris.VERSION + Uris.PRODUCTS)
public class ProductResource {

    private ProductController productController;

    @Autowired
    public void setProductController(ProductController productController) {
        this.productController = productController;
    }

    // @PreAuthorize("hasRole('ADMIN')or hasRole('MANAGER') or hasRole('OPERATOR')")
    @RequestMapping(value = Uris.CODE, method = RequestMethod.GET)
    public ProductWrapper getProductByCode(@PathVariable(value = "code") String code) throws NotFoundProductCodeException {
        throwExceptionIfProductDoesNotExists(code);
        return new ProductWrapper(productController.getProductByCode(code));
    }

    @RequestMapping(value = Uris.CODE, method = RequestMethod.PATCH)
    public void setProductAsDiscontinued(@PathVariable String code,
            @RequestBody List<PatchChangeDescriptionWrapper> patchChangeDescriptionsWrapper) throws NotFoundProductCodeException {
        throwExceptionIfProductDoesNotExists(code);
        for (PatchChangeDescriptionWrapper patchRequestBodyWrapper : patchChangeDescriptionsWrapper) {
            String operation = patchRequestBodyWrapper.getOp();
            String path = patchRequestBodyWrapper.getPath();
            String value = patchRequestBodyWrapper.getValue();
            if (operation.equals(PatchOperations.REPLACE)) {
                if (path.equals(Uris.DISCONTINUED)) {
                    productController.setProductAsDiscontinued(code, Boolean.parseBoolean(value));
                }
            }
        }
    }

    @RequestMapping(value = Uris.BARCODES, method = RequestMethod.POST)
    public ResponseEntity<byte[]> generateBarcodesPdf(@RequestBody List<ProductBarcodeWrapper> productBarcodeWrappers)
            throws NotFoundProductCodeException, IOException {
        productBarcodeWrappers = productBarcodeWrappers.stream()
                .filter(barcodeWrapper -> barcodeWrapper.getBarcode() != null && !barcodeWrapper.getBarcode().isEmpty())
                .collect(Collectors.toList());
        for (ProductBarcodeWrapper productBarcodeWrapper : productBarcodeWrappers) {
            throwExceptionIfProductDoesNotExists(productBarcodeWrapper.getBarcode());
        }
        byte[] pdfByteArray = productController.generateBarcodesPdf(productBarcodeWrappers);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "barcodes.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-" + "hcheck=0");
        ResponseEntity<byte[]> barcodesPdf = new ResponseEntity<byte[]>(pdfByteArray, headers, HttpStatus.OK);
        return barcodesPdf;
    }

    private void throwExceptionIfProductDoesNotExists(String code) throws NotFoundProductCodeException {
        Product product = productController.getProductByCode(code);
        if (product == null) {
            throw new NotFoundProductCodeException("Product code: " + code);
        }
    }
}
