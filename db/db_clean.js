//mongo localhost:27017/SmartRead db_clean.js
//load("db_clean.js")

db = db.getSiblingDB('SmartRead');
var booksCursor = db.bx_books.find({ $or: [{ title: '' }, { author: '' }, { isbn: '' }, { publisher: '' }, { year: '' }] });
while (booksCursor.hasNext()) {
    var book = booksCursor.next();
    db.bx_ratings.remove({ isbn: book.isbn });
}
db.bx_books.remove({ $or: [{ title: '' }, { author: '' }, { isbn: '' }, { publisher: '' }, { year: '' }] });