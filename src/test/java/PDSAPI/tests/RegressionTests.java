package PDSAPI.tests;

import PDSAPI.actions.*;
import PDSAPI.models.*;
import PDSAPI.specs.ConstantValues;
import helpers.CreatePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;


public class RegressionTests {
    private String sessionToken, calcID, policyID, policyNumber;
    PolicyImport policy = new CreatePolicy().getPolicy();

    @BeforeEach
    public void setUp() {
        sessionToken = Auth.loginUser(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        ImportResponse response = Import.getImportResponse(sessionToken, policy);
        calcID = Import.getCalcID(response);
        policyID = Import.getPolicyID(response);
        policyNumber = Import.getNumber(response);

        Attach.AttachDocs(sessionToken, calcID, "Документ, удостоверяющий личность");
        Attach.AttachDocs(sessionToken, calcID, "Анкета для проведения идентификации клиента");
        Attach.AttachDocs(sessionToken, calcID, "Согласие на обработку ПД");
        Attach.AttachDocs(sessionToken, calcID, "Согласие на доп. услугу");
    }


    @Test
    @Tag("regression")
    public void issue(TestInfo testInfo){
        Issue.IssuePolicy(sessionToken, policyID);
        System.out.println("=== ТЕСТ: " + testInfo.getDisplayName() + " ===");
        System.out.println("Метка: " + testInfo.getTags());
        System.out.println("Полис оформлен: " + policyNumber);
    }

    @Test
    @Tag("regression")
    public void issueWithAnnulate(TestInfo testInfo){
        Issue.IssuePolicy(sessionToken, policyID);
        Annul.AnnulPolicy(sessionToken, calcID);
        System.out.println("=== ТЕСТ: " + testInfo.getDisplayName() + " ===");
        System.out.println("Метка: " + testInfo.getTags());
        System.out.println("Полис аннулирован: " + policyNumber);
    }

}
