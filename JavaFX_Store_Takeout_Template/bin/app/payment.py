from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route("/payment", methods=["POST"])
def handle_payment():
    data = request.json
    item_name = data.get("item")
    amount = data.get("amount")
    
    print(f"결제 요청: {item_name} - {amount}원")

    # 여기서 실제 결제 로직 대신 모의결과 반환
    return jsonify({"status": "success", "message": f"{amount}원 결제 완료"})

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000)