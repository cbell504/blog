package dev.christopherbell.thevoid.invite;

import dev.christopherbell.libs.common.api.exceptions.ResourceNotFoundException;
import dev.christopherbell.libs.common.api.exceptions.InvalidRequestException;
import dev.christopherbell.libs.common.api.exceptions.InvalidTokenException;
import dev.christopherbell.libs.common.api.utils.APIValidationUtils;
import dev.christopherbell.thevoid.common.VoidRequest;
import dev.christopherbell.thevoid.common.VoidResponse;
import dev.christopherbell.thevoid.account.model.dto.Account;
import dev.christopherbell.thevoid.account.AccountMessenger;
import dev.christopherbell.thevoid.permission.PermissionsService;
import dev.christopherbell.thevoid.utils.ValidateUtil;
import dev.christopherbell.thevoid.utils.mappers.MapStructMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class InviteCodeService {

  private final AccountMessenger accountMessenger;
  private final InviteCodeMessenger inviteCodeMessenger;
  private final MapStructMapper mapStructMapper;
  private final PermissionsService permissionsService;

  /**
   *
   * @param clientId
   * @param loginToken
   * @param voidRequest
   * @return
   * @throws InvalidRequestException
   * @throws ResourceNotFoundException
   * @throws InvalidTokenException
   */
  public VoidResponse generateInviteCode(String clientId, String loginToken, VoidRequest voidRequest)
      throws InvalidRequestException, ResourceNotFoundException, InvalidTokenException {
    ValidateUtil.validateAccount(voidRequest);
    APIValidationUtils.isValidClientId(ValidateUtil.ACCEPTED_CLIENT_IDs, clientId);

    var account = Objects.requireNonNullElse(voidRequest.getAccount(), new Account());
    var accountId = Objects.requireNonNullElse(account.getId(), 0L);

    var isValidCreds = this.permissionsService.validateLoginToken(loginToken, accountId);
    if (!isValidCreds) {
      throw new InvalidTokenException("Invalid token.");
    } else {
      // Build a template success response
      var accountEntity = this.accountMessenger.getAccountEntityById(accountId);
      var inviteCodeEntity = new InviteCodeEntity();
      inviteCodeEntity.setAccountEntity(accountEntity);
      inviteCodeEntity.setCode(UUID.randomUUID().toString());
      // Save the invite Code
      this.inviteCodeMessenger.saveInviteCode(inviteCodeEntity);

      return VoidResponse.builder()
          .inviteCode(mapStructMapper.mapToInviteCode(inviteCodeEntity))
          .build();
    }
  }
}