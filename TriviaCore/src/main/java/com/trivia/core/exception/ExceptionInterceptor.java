package com.trivia.core.exception;


import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.NoResultException;
import java.io.Serializable;



/**
 *  I think it is a good idea to be *very* explicit and conservative about exception management and
 *  the amount of details we leak to our clients/users, even our own modules.
 *
 *  Exception management starts here and propagates to the lower modules: API via BusinessExceptionMapper
 *  for Responses and Admin via BusinessExceptionHandler for View Messages. We give only what we absolutely need to.
 *
 *  The reason why we are rethrowing some exceptions using exceptions extending BusinessException
 *  is to decouple our exception system from any other external exception system, and to ensure flexibility.
 *
 *  In the future, an intricate system of error reporting (via general error codes and specific error id's)
 *  can easily be created.
 *
 *  TODO: Unfortunately, since Widlfly's own AuthorizationInterceptor gets invoked before any of our own Interceptors
 *  can, we need to deal with EJBAccessException separately.
 */
@Throws
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class ExceptionInterceptor implements Serializable {
    @AroundInvoke
    public Object checkForExceptions(InvocationContext invocationContext) {
        try {
            return invocationContext.proceed();
        }
        catch (javax.persistence.EntityNotFoundException | NoResultException e) {
            throw new EntityNotFoundException();
        }
        catch (javax.persistence.EntityExistsException e) {
            throw new EntityExistsException();
        }
        // SystemException at this point means something is wrong in the Core Module itself, so any details are withheld.
        catch (SystemException e) {
            e.printStackTrace();
            throw new SystemException();
        }
        // Other BusinessExceptions have necessary information so we rethrow them.
        catch (BusinessException e) {
            throw e;
        }
        // We don't want to propagate any other exceptions, as this could be a security issue.
        catch (Exception e) {
            e.printStackTrace();
            throw new SystemException();
        }
    }
}