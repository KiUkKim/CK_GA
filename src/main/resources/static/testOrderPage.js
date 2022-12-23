// <!-- iamport.payment.js -->
// <script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
//
// // 주문번호 만들기
// function createOrderNum(){
//     const date = new Date();
//     const year = date.getFullYear();
//     const month = String(date.getMonth() + 1).padStart(2, "0");
//     const day = String(date.getDate()).padStart(2, "0");
//
//     let orderNum = "customer";
//     for(let i=0;i<4;i++) {
//         orderNum += Math.floor(Math.random() * 8);
//     }
//     return orderNum;
// }
//
//
// // 카드 결제
// function paymentCard(data) {
//     // 모바일로 결제시 이동페이지
//     const pathName = location.pathname;
//     const href = location.href;
//     const m_redirect = href.replaceAll(pathName, "");
//
//     IMP.init("가맹점 식별코드");
//
//     IMP.request_pay({ // param
//             pg: "kcp",
//             pay_method: data.payMethod,
//             merchant_uid: data.orderNum,
//             name: data.name,
//             amount: data.amount,
//             buyer_email: data.buyer_email,
//             buyer_name: data.buyer_name,
//             buyer_tel: data.buyer_tel,
//             buyer_addr: data.deleveryAddress2 + " " + data.deleveryAddress3,
//             buyer_postcode: data.deleveryAddress1,
//             m_redirect_url: m_redirect,
//         },
//         function (rsp) { // callback
//             if (rsp.success) {
//                 // 결제 성공 시 로직,
//                 data.impUid = rsp.imp_uid;
//                 data.merchant_uid = rsp.merchant_uid;
//                 paymentComplete(data);
//
//             } else {
//                 // 결제 실패 시 로직,
//             }
//         });
// }
//
// // 계산 완료
// function paymentComplete(data) {
//
//     $.ajax({
//         url: "/api/order/payment/complete",
//         method: "POST",
//         data: data,
//     })
//         .done(function(result) {
//             messageSend();
//             swal({
//                 text: result,
//                 closeOnClickOutside : false
//             })
//                 .then(function(){
//                     location.replace("/orderList");
//                 })
//         }) // done
//         .fail(function() {
//             alert("에러");
//             location.replace("/");
//         })
// }
//
// function payment()
// {
//     const data = {
//         payMethod: $("input[type='radio']:checked").val(),
//         orderNum : createOrderNum(),
//         name : $(".order_info li").eq(0).find(".food_name").text(),
//         amount: Number($("total").val()) - Number($(".point_input").val()),
//         phone : $("input[name='phone']").val(),
//         request: $("textarea[name='request']").val(),
//         usedPoint: $("input[name='usedPoint']").val(),
//         deliveryAddress1 : $("#deliveryAddress1").val(),
//         deliveryAddress2 : $("#deliveryAddress2").val(),
//         deliveryAddress3 : $("#deliveryAddress3").val(),
//         totalPrice : $("#total").val()
//     }
//
//     if(!data.deliveryAddress1 || !data.deliveryAddress2)
//     {
//         swal('배달 받으실 주소를 입력해 주세요')
//         return;
//     }
//
//     if($(".order_info li").length < 1){
//         return;
//     }
//
//     if(!data.phone){
//         swal('전화번호를 입력해주세요');
//         return;
//     }
//
//     if(data.payMethod == "현장결제"){
//         paymentCash(data);
//         return;
//     }
//
//     payMentCard(data);
// }
