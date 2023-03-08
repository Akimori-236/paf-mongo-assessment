package nus.iss.tfip.pafmongoassessment.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    private final String MIN_TRANSFER_AMT = "10.00";

    private String id;
    @NotEmpty(message = "From account cannot be empty")
    // C1
    @Size(min=10, max=10, message ="Account ID length must be 10")
    private String fromAccount;
    private String fromAccountName;
    @NotEmpty(message = "To account cannot be empty")
    // C1
    @Size(min=10, max=10, message ="Account ID length must be 10")
    private String toAccount;
    private String toAccountName;
    // C3
    @NotNull(message = "Amount cannot be empty")
    @Positive(message = "Amount must be a positive number")
    // C4
    @DecimalMin(value = MIN_TRANSFER_AMT, message = "Minimum amount is $" + MIN_TRANSFER_AMT)
    @Digits(integer = 50, fraction = 2, message = "Maximum amount is 50 digits and 2 decimal places")
    private Double amount;
    private String comments;

}
