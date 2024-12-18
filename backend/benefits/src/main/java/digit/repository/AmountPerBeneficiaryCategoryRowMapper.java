package digit.repository;

import digit.web.models.AmountPerBeneficiaryCategory;
import digit.web.models.EligibilityCriteria;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AmountPerBeneficiaryCategoryRowMapper implements RowMapper<AmountPerBeneficiaryCategory> {
    @Override
    public AmountPerBeneficiaryCategory mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        AmountPerBeneficiaryCategory amountPerBeneficiaryCategory=new AmountPerBeneficiaryCategory();
        amountPerBeneficiaryCategory.setId(rs.getString("id"));
        amountPerBeneficiaryCategory.setBeneficiaryAmount(rs.getDouble("amount"));
        amountPerBeneficiaryCategory.setBeneficiaryCategory(rs.getString("category"));

        String studenTypeString=rs.getString("type");
        EligibilityCriteria.StudentTypeEnum studentTypeEnum= EligibilityCriteria.StudentTypeEnum.fromValue(studenTypeString);
        amountPerBeneficiaryCategory.setBeneficiaryType(studentTypeEnum);

        String casteString=rs.getString("caste");
        EligibilityCriteria.CasteEnum catseEnum= EligibilityCriteria.CasteEnum.fromValue(casteString);
        amountPerBeneficiaryCategory.setBeneficiaryCaste(catseEnum);

        return amountPerBeneficiaryCategory;
       /* return AmountPerBeneficiaryCategory.builder()
                .id(rs.getString("id"))
                .beneficiaryType(EligibilityCriteria.StudentTypeEnum.valueOf(rs.getString("beneficiaryType")))
                .beneficiaryCaste(EligibilityCriteria.CasteEnum.valueOf(rs.getString("beneficiaryCaste")))
                .beneficiaryCategory(rs.getString("beneficiaryCategory"))
                .beneficiaryAmount(rs.getDouble("beneficiaryAmount"))
                .build();*/
    }
}
