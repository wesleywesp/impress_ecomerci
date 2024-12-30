package impress.weasp.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(AdminException.class)
    public ResponseEntity<ErrorDetails> sellerExceptionHandler(AdminException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> productExceptionHandler(ProductException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustumerException.class)
    public ResponseEntity<ErrorDetails> custumerExceptionHandler(CustumerException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ErrorDetails> JwtTokenExceptionHandler(JwtTokenException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AutenficacaoException.class)
    public ResponseEntity<ErrorDetails> AutenficacaoExceptionHandler(AutenficacaoException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CartException.class)
    public ResponseEntity<ErrorDetails> CartException(AutenficacaoException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorDetails> OrderException(AutenficacaoException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AdminReportExecption.class)
    public ResponseEntity<ErrorDetails> SellreportExecption(AutenficacaoException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PaymentException.class)
        public ResponseEntity<ErrorDetails> handlePaymentException(PaymentException ex, WebRequest request) {
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(ex.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }


    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ErrorDetails> ReviewException(AutenficacaoException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ErrorDetails> CouponException(AutenficacaoException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HomeCategoryException.class)
    public ResponseEntity<ErrorDetails> HomeCategoryException(AutenficacaoException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DealException.class)
    public ResponseEntity<ErrorDetails> DealException(AutenficacaoException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setError(se.getMessage());
        errorDetails.setTimestamp(java.time.LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
