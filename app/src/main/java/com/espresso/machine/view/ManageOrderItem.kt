package com.espresso.machine.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.espresso.machine.R
import com.espresso.machine.model.entity.MilkType
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.model.entity.Size
import com.espresso.machine.model.repository.FoodRepository
import com.espresso.machine.model.repository.OrderItemRepository
import com.espresso.machine.viewModel.ManageOrderItemViewModel
import com.espresso.machine.viewModel.ManageOrderItemViewModelFactory
import com.espresso.machine.viewModel.OrderItemState

@Composable
fun ManageOrderItemScreen(
    orderItemRepository: OrderItemRepository,
    foodRepository: FoodRepository,
    loadFoodItemDetails: Int? = null,
    orderId : Int,
    updateOrderItemId : Int? = null,
    navigateUp: () -> Unit
){
    val viewModel = viewModel<ManageOrderItemViewModel>(
        factory = ManageOrderItemViewModelFactory(orderItemRepository,foodRepository, loadFoodItemDetails, updateOrderItemId)
    )

    val orderItem = OrderItem(
        foodItemId = viewModel.state.foodItemId,
        isUpdated = viewModel.state.isUpdated,
        size = viewModel.state.size,
        milkType = viewModel.state.milkType,
        itemPriceTotal = viewModel.state.itemPriceTotal,
        quantity = viewModel.state.quantity,
        orderId = orderId,
        foodName = viewModel.state.foodName,
        foodResId = viewModel.state.foodResId,
    )

    ManageOrderItem(
        navigateUp = { navigateUp() },
        state = viewModel.state,
        addToOrder = { viewModel.addOrderItem(orderItem) },
        updateOrder = { viewModel.updateOrderItem(orderItem)},
        increaseQuantity = {viewModel.increaseQuantity()},
        decreaseQuantity = {viewModel.decreaseQuantity()},
        onSizeChange = viewModel::onSizeChange,
        onMilkTypeChange = viewModel::onMilkTypeChange,
        onItemPriceTotalChange = viewModel::onItemPriceTotalChange,
        itemName = viewModel.food?.name ?: viewModel.state.foodName,
        itemDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
        painter = when { viewModel.food?.resId != null -> painterResource(viewModel.food?.resId!!)
            else -> painterResource(viewModel.state.foodResId)
        },
    )

}

@Composable
private fun ManageOrderItem(
    navigateUp: () -> Unit,
    state: OrderItemState,
    addToOrder: () -> Unit,
    updateOrder: () -> Unit,
    increaseQuantity: () -> Unit,
    decreaseQuantity: () -> Unit,
    onSizeChange: (String) -> Unit,
    onMilkTypeChange: (String) -> Unit,
    onItemPriceTotalChange: () -> Unit,
    itemName: String? = "Default Name",
    itemDescription: String? = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
    painter: Painter? = null
){
    Column(
        modifier = Modifier
            .padding(vertical = 25.dp, horizontal = 10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(411.dp)
                .background(color = Color(0xFFF8BEB0), shape = RoundedCornerShape(size = 25.dp))
        ) {
            // TODO add liked items state

            Box(
                modifier = Modifier
                    .clickable { navigateUp() }
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .padding(1.dp)
                    .size(40.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color(0xFFF4F4F4), shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Back to main menu"
                )
            }

            if (painter != null) {
                Image(
                    painter,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(2.dp)
                )
            }else{
                Text(
                    text = "Could not load the image",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (itemName != null) {
                Text(text = itemName,
                    style = TextStyle(
                        fontSize = 20.sp)
                )
            }
            Icon(
                painter = painterResource(R.drawable.mdi_heart_filled),
                contentDescription = "Back to main menu"
            )
        }

        if (itemDescription != null) {
            Text(text = itemDescription)
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)){
            Text(text = "Size",
                style = TextStyle(
                    fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                ChoicesButton(onChoiceChange = { onSizeChange(Size.SMALL.name) }, isSelected = (state.size == Size.SMALL), choice = Size.SMALL.name)
                ChoicesButton(onChoiceChange = { onSizeChange(Size.MEDIUM.name) }, isSelected = (state.size == Size.MEDIUM), choice = Size.MEDIUM.name)
                ChoicesButton(onChoiceChange = { onSizeChange(Size.LARGE.name) }, isSelected = (state.size == Size.LARGE), choice = Size.LARGE.name)
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)){
            Text(text = "Choice of Milk",
                style = TextStyle(
                    fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                ChoicesButton(onChoiceChange = { onMilkTypeChange(MilkType.OAT.name) }, isSelected = (state.milkType == MilkType.OAT), choice = MilkType.OAT.name)
                ChoicesButton(onChoiceChange = { onMilkTypeChange(MilkType.ALMOND.name) }, isSelected = (state.milkType == MilkType.ALMOND), choice = MilkType.ALMOND.name)
                ChoicesButton(onChoiceChange = { onMilkTypeChange(MilkType.COCONUT.name) }, isSelected = (state.milkType == MilkType.COCONUT), choice = MilkType.COCONUT.name)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ){
            Text(
                text = "Quantity",
                style = TextStyle(
                fontSize = 20.sp)
            )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current

                Button(
                    modifier = Modifier
                        .width(26.dp)
                        .height(30.49773.dp),
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 10.dp
                    ),
                    onClick = {
                        if (state.quantity > 0) {
                            decreaseQuantity()
                            onItemPriceTotalChange()
                        } else {
                            Toast.makeText(context,"Order quantity cannot be less than zero", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("-")
                }

                Text("${state.quantity}")

                Button(
                    modifier = Modifier
                        .width(26.dp)
                        .height(30.49773.dp),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 10.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 0.dp
                    ),
                    onClick = {
                        increaseQuantity()
                        onItemPriceTotalChange()
                    }
                ) {
                    Text( textAlign = TextAlign.Center, color = Color.White, text = "+")
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier.width(70.dp)
            ) {
                Text(
                    modifier = Modifier
                        .width(45.dp)
                        .height(22.dp),
                    text = "Price",
                    style = TextStyle(
                        fontSize = 18.sp,
                        //fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFAEABAB),
                    )
                )
                Text(
                    text = "£${state.itemPriceTotal}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        //fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                    )
                )
            }
            Button(
                modifier = Modifier
                    .width(245.93684.dp)
                    .height(39.88165.dp)
                    .background(color = Color(0xFFFCE1C9), shape = RoundedCornerShape(size = 15.dp)),
                onClick = {
                    if (state.isUpdated) {
                        updateOrder.invoke()
                    } else {
                        addToOrder.invoke()
                        navigateUp()
                    }
                },
                enabled = !(!state.isUpdated && state.quantity == 0) //if isupdating is true and qty is 0 them perform onRemoveItem
            ) {
                Text(
                    text = if (state.isUpdated) {
                        "Update Cart" }
                     else {
                         "Add Cart"
                     }
                )
            }
        }
    }
}

@Composable
fun ChoicesButton(
    onChoiceChange: (String) -> Unit,
    isSelected: Boolean,
    choice: String
){

    val black: Color = Color.Black
    val white: Color = Color.White

    Button(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color(0xFF000000),
                shape = RoundedCornerShape(size = 15.dp)
            )
            .width(107.15405.dp)
            .height(40.dp)
            .background(
                color = if (isSelected) black else white,
                shape = RoundedCornerShape(size = 15.dp)
            )
            .padding(2.dp),
        onClick = {onChoiceChange(choice)},
    ){
        Text(
            text = choice,
            style = TextStyle(
                fontSize = 18.sp,
                color = if (isSelected) white else black,
            )
        )
    }
}

/*
@Preview (showSystemUi = true)
@Composable
fun PreviewManageCart(){
    ManageCart()
}*/
