package vn.vme.controller;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import vn.vme.common.JCode.CommonCode;
import vn.vme.common.JCode.UserCode;
import vn.vme.exception.AccessDeniedException;
import vn.vme.exception.ExistedDataException;
import vn.vme.exception.ExistedEmailException;
import vn.vme.exception.ExistedNameException;
import vn.vme.exception.ExistedPhoneException;
import vn.vme.exception.InvalidDataException;
import vn.vme.exception.InvalidKeyException;
import vn.vme.exception.KeyExpriredException;
import vn.vme.exception.NotEnoughBalanceException;
import vn.vme.exception.NotFoundException;
import vn.vme.exception.UserLockedException;
import vn.vme.exception.UserNotConfirmedException;
import vn.vme.exception.UserNotFoundException;
import vn.vme.model.Rest;
import vn.vme.service.I18nService;

@ControllerAdvice
@RestController
public class ControllerException {

	@Autowired
	I18nService i18n;

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<Rest> handleIllegalArgumentException(Exception ex, WebRequest request)
			throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.REQUEST_INVALID.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Rest> UserNotFoundException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.NOT_FOUND.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<Rest> notFoundException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.NOT_FOUND.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(AccessDeniedException.class)
	public final ResponseEntity<Rest> access(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.ACCESS_DENY.code, i18n.msg(CommonCode.ACCESS_DENY.code));
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public final ResponseEntity<Rest> accessDeny(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.ACCESS_DENY.code, i18n.msg(CommonCode.ACCESS_DENY.code));
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(UserLockedException.class)
	public final ResponseEntity<Rest> UserLockedException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(UserCode.USER_BLOCK.code, i18n.msg(UserCode.USER_BLOCK.code));
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(KeyExpriredException.class)
	public final ResponseEntity<Rest> KeyExpriredException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(UserCode.OTP_FAIL.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserNotConfirmedException.class)
	public final ResponseEntity<Rest> userNotConfirmed(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(UserCode.NOT_ACTIVE.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
	@ExceptionHandler(NotEnoughBalanceException.class)
	public final ResponseEntity<Rest> balance(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(UserCode.NOT_ENOUGHT_BALANCE.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(InvalidKeyException.class)
	public final ResponseEntity<Rest> DataException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.SIGN_INVALID.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.NOT_ACCEPTABLE);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ExistedPhoneException.class)
	public final ResponseEntity<Rest> existedCodeException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.CONFLICT.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ExistedEmailException.class)
	public final ResponseEntity<Rest> existedEmailException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.CONFLICT.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ExistedDataException.class)
	public final ResponseEntity<Rest> existedDataException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.CONFLICT.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ExistedNameException.class)
	public final ResponseEntity<Rest> duplicateDataException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.CONFLICT.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(InvalidDataException.class)
	public final ResponseEntity<Rest> invalidDataException(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.CONFLICT.code, ex.getMessage());
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Rest> handleUnknown(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.SYSTEM_ERROR.code, i18n.msg(CommonCode.SYSTEM_ERROR.code));
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchElementException.class)
	public final ResponseEntity<Rest> notFound(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.NOT_FOUND.code, i18n.msg(CommonCode.NOT_FOUND.code));
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InvalidGrantException.class)
	public final ResponseEntity<Rest> handleUnknownRuntime(Exception ex, WebRequest request) throws IOException {
		ex.printStackTrace();
		Rest rest = new Rest(CommonCode.ACCESS_DENY.code, i18n.msg(CommonCode.ACCESS_DENY.code));
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}
}
